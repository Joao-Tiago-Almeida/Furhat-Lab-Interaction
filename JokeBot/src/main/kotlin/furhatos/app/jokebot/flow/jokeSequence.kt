package furhatos.app.jokebot.flow

import furhatos.app.jokebot.jokes.*
import furhatos.app.jokebot.name
import furhatos.app.jokebot.nlu.BadJoke
import furhatos.app.jokebot.nlu.GoodJoke
import furhatos.app.jokebot.util.calculateJokeScore
import furhatos.app.jokebot.wantsJoke
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.skills.emotions.UserGestures

/**
 * This state gets jokes, tells jokes, and records the user's response to it.
 *
 * Once the joke has been told, prompts the user if they want to hear another joke.
 */
val JokeSequence: State = state(Interaction) {

    onEntry {
        //Get joke from the JokeHandler

        random(
            {furhat.say("Alright ${users.current.name}")},
            {furhat.say("Okay ${users.current.name}")},
            {furhat.say("Alright then, ${users.current.name}")},
            {furhat.say("${users.current.name}")}
        )

        val joke = JokeHandler.getJoke()

        //Build an utterance with the joke.
        val utterance = utterance {
            +getJokeComment(joke.score) //Get comment on joke, using the score
            +delay(200) //Short delay
            +joke.intro //Deliver the intro of the joke
            +delay(500) //Short delay
            +joke.punchline
        }

        furhat.say(utterance)
        
        /**
         * Calls a state which we know returns a joke score.
         */
        JokeHandler.changeJokeScore(call(JokeScore) as Double)

        //Prompt the user if they want to hear another joke.
        furhat.ask(continuePrompt.random())
    }

    onResponse<Yes> {
        random(
            {furhat.say("Sweet!")},
            {furhat.say("Awesome!")},
            {furhat.say("Great!")}
        )
        furhat.attend(users.random)
        reentry()
    }

    onResponse<No> {
        random(
            {furhat.say(utterance{+ blocking {
                        furhat.gesture(Gestures.BigSmile, async = false)
                        }
                            + "Alright, thanks for letting me practice!"})},
            {furhat.say(utterance {+ blocking {
                        furhat.gesture(Gestures.BigSmile, async = false)
                        }
                            + "Okay, it was nice though. Have a good day!"})},
            {furhat.say(utterance {+ "Alright"
                            + blocking {
                                furhat.gesture(Gestures.Wink, async = false)
                            }
                            + "It was nice meeting you!"})}
        )

        users.current.wantsJoke = false

        if (users.other.wantsJoke == true) {
            furhat.attend(users.other)
            random(
                {furhat.say("How about you.")},
                {furhat.say("Maybe you.")},
                {furhat.say("Perhaps you.")},
                {furhat.say("I will have a shoot on you.")},
                {furhat.say("Let me focus on you now.")}
            )
            goto(AreYouHappy)
        } else {
            goto(Idle)
        }
    }

    onNoResponse {
        random(
            {furhat.say("Sorry, I didn't hear you.")},
            {furhat.say("Sorry, could you repeat that?")},
            {furhat.say("Sorry, could you say that one more time?")}
        )
        random(
            {furhat.ask("Yes or no?")},
            {furhat.ask("Sorry, could you try a shorter answer. Perhaps yes or no?")},
            {furhat.ask("Sorry, you give me a yes or no answer?")}
        )
    }
}
/**
 * This state records the users' reaction to a joke, and terminates with calculated joke value.
 * Joke value is based on if the user smiled and/or the user said it was a good/bad joke.
 */
val JokeScore: State = state(Interaction) {

    var saidBadJoke = false
    var saidGoodJoke = false
    var timeSmiledAtJoke = 0L
    var timestampStartedLaughing = 0L
    val jokeTimeout = 4000 // We wait for a reaction for 4 seconds

    onEntry {
        furhat.listen()
    }

    onResponse<GoodJoke>(instant = true) {
        saidGoodJoke = true
        furhat.say("Good Joke")
    }

    onResponse<BadJoke>(instant = true) {
        saidBadJoke = true
        furhat.say("Bad Joke")
    }

    onResponse(instant = true) {
        //Do nothing
        furhat.say("At least a response")
    }

    onNoResponse(instant = true) {
        //Do nothing
        furhat.say("I see, no response")
    }

    onUserGesture(UserGestures.Smile, cond = { it.isCurrentUser }, instant = true) {
        timestampStartedLaughing = System.currentTimeMillis() //Timestamp the moment the user started smiling.
        propagate()
    }

    onUserGestureEnd(UserGestures.Smile, cond = { it.isCurrentUser }, instant = true) {
        timeSmiledAtJoke += System.currentTimeMillis() - timestampStartedLaughing //Calculating the amount of millis spent laughing
        timestampStartedLaughing = 0L
        propagate()
    }

    onTime(delay = jokeTimeout) {
        if (timestampStartedLaughing != 0L) {
            timeSmiledAtJoke += System.currentTimeMillis() - timestampStartedLaughing
        }

        furhat.say(getResponseOnUser(timeSmiledAtJoke != 0L, saidBadJoke, saidGoodJoke))

        terminate(calculateJokeScore(timeSmiledAtJoke, jokeTimeout, saidBadJoke, saidGoodJoke))
    }
}
