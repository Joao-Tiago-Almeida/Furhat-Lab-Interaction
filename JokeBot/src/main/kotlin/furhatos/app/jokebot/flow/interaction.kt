package furhatos.app.jokebot.flow

import furhatos.app.jokebot.name
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Start : State = state(Interaction) {

    onEntry {
        random(
            {furhat.ask("Hi there. It is a pleasure to meet you. I am James. What is your name?")},
            {furhat.ask("Hello my friend. Glad to meet you! My name is James. What is your name?")},
            {furhat.ask("Hi there. What a pleasure meeting you. My name is James. What is your name?")}
        )
    }

    onReentry {
        random(
            {furhat.ask("What is your name?")},
            {furhat.ask("What was your name?")},
            {furhat.ask("Your name is what?")}
        )
    }

    onNoResponse {
        random(
            {furhat.ask("Hello? Sorry, you might have missed that. I was asking for your name?")},
            {furhat.ask("Hello, somebody out there? My name is James, what is yours?")},
            {furhat.ask("Did you say something? Sorry I might have missed that. What is your name again?")}
        )
        //goto(AreYouHappy)
    }
}

val AreYouHappy: State = state(Interaction) {

    onEntry {
        furhat.ask("${users.current.name}, I am wondering, are you happy today?")
    }

    onResponse<Yes> {
        random(
            {furhat.say(utterance {+"Great to hear, then you are in the right mood!"
                        + Gestures.Wink})},
            {furhat.say(utterance {+Gestures.Smile
                        + "Awesome! then let us start right away."})}
        )
        goto(RequestJokeTest)
    }

    onResponse<No> {
        random(
            {furhat.say(utterance {+"I'm sorry to hear that."
                        + Gestures.ExpressSad
                        + "Hmm, perhaps we can do something to cheer you up."})},
            {furhat.say(utterance {+"That is sad to hear"
                        + Gestures.Thoughtful
                        + "Maybe I can cheer you up with this..."})}
        )
        goto(RequestJokeTest)
    }

    onResponse {
        random(
            {furhat.say(utterance {+"Perhaps we can try to increase your happiness a few notches."
                        + Gestures.Wink})},
            {furhat.say(utterance {+"Let me see whether I can increase your happiness a few intches"
                        + Gestures.Smile})}
        )
        goto(RequestJokeTest)
    }
}

val RequestJokeTest: State = state(Interaction) {
    onEntry {
        random(
            {furhat.ask(utterance {+"I’m trying to learn some humor, you see. So. Could I test a few jokes on you?"
                        + Gestures.Smile})},
            {furhat.ask(utterance {+"My creator told me I should try to be a bit funnier. Could I test a few jokes on you?"
                        + Gestures.Smile})},
            {furhat.ask(utterance {+ "My designer always complains that I am not funny enough. Hence I learned a few jokes."
                        + Gestures.Wink
                        + "Could I test them out on you?"})}
        )
    }

    onResponse<Yes> {
        random(
            {furhat.say("Awesome")},
            {furhat.say("Great!")},
            {furhat.say("Wonderful")}
        )
        goto(JokeSequence)
    }

    onResponse<No> {
        random(
            {furhat.say("Oh, that’s a shame.")},
            {furhat.say("What a pity.")},
            {furhat.say("That is sad")}
        )
        furhat.say("But I respect that. There are certainly more funny things than a joking robot")
        goto(Idle)
    }
}
