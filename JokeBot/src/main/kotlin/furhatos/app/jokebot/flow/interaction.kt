package furhatos.app.jokebot.flow

import furhatos.app.jokebot.name
import furhatos.app.jokebot.wantsJoke
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures


val Start : State = state(Interaction) {

    onEntry {
        random(
            {furhat.ask("And what about you? What is your name?")},
            {furhat.ask("And what is your name?")},
            {furhat.ask("And your name is what?")}
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

val SelfPresent : State = state(Start){
    onEntry {
        random(
            {furhat.ask("Hi there. It is a pleasure to meet you. I am James. What is your name?")},
            {furhat.ask("Hello my friend. Glad to meet you! My name is James. What is your name?")},
            {furhat.ask("Hi there. What a pleasure meeting you. My name is James. What is your name?")}
        )
    }
}

val AreYouHappy: State = state(Interaction) {

    onEntry {
        furhat.attend(users.random)

        random(
            {furhat.ask("${users.current.name}, I am wondering, are you happy today?")},
            {furhat.ask("${users.current.name}, tell me, are you happy today?")},
            {furhat.ask("${users.current.name}, are you happy today?")}
        )
    }

    onResponse<Yes> {
        random(
            {furhat.say(utterance {+"Great to hear, then you are in the right mood!"
                            + blocking {
                                furhat.gesture(Gestures.Wink, async = false)
                            }})},
            {furhat.say(utterance {
                        + blocking {
                            furhat.gesture(Gestures.BigSmile, async = false)
                                    }
                        + "Awesome! then let us start right away."})}
        )
        goto(RequestJokeTest)
    }

    onResponse<No> {
        random(
            {furhat.say(utterance {+"I'm sorry to hear that."
                        + blocking {
                            furhat.gesture(Gestures.ExpressSad, async = false)
                        }
                        + "Hmm, perhaps we can do something to cheer you up."})},
            {furhat.say(utterance {+"That is sad to hear."
                        + blocking {
                            furhat.gesture(Gestures.Thoughtful, async = false)
                        }
                        + "Maybe I can cheer you up with this..."})}
        )
        goto(RequestJokeTest)
    }

    onResponse {
        random(
            {furhat.say(utterance {+"Perhaps we can try to increase your happiness a few notches."
                            + blocking {
                                furhat.gesture(Gestures.Wink, async = false)
                            }})},
            {furhat.say(utterance {+"Let me see whether I can increase your happiness a few intches"
                            + blocking {
                                furhat.gesture(Gestures.BigSmile, async = false)
                            }})}
        )
        goto(RequestJokeTest)
    }
}

val RequestJokeTest: State = state(Interaction) {
    onEntry {
        random(
            {furhat.ask(utterance {+"I’m trying to learn some humor, you see. So. Could I test a few jokes on you?"
                            + blocking {
                                furhat.gesture(Gestures.BigSmile, async = false)
                            }})},
            {furhat.ask(utterance {+"My creator told me I should try to be a bit funnier. Could I test a few jokes on you?"
                            + blocking {
                                furhat.gesture(Gestures.BigSmile, async = false)
                            }})},
            {furhat.ask(utterance {+ "My designer always complains that I am not funny enough. Hence I learned a few jokes."
                        + blocking {
                            furhat.gesture(Gestures.Wink, async = false)
                        }
                        + "Could I test them out on you?"})}
        )
    }

    onReentry {
        random(
            {furhat.ask(utterance {+"${users.current.name}, could I test a few jokes on you?"
                + blocking {
                    furhat.gesture(Gestures.BigSmile, async = false)
                }})},
            {furhat.ask(utterance {+"Could I test a few jokes on you?"
                + blocking {
                    furhat.gesture(Gestures.BigSmile, async = false)
                }})},
            {furhat.ask(utterance {+"Could I test some jokes out on you ${users.current.name}?"
                    + blocking {
                furhat.gesture(Gestures.BigSmile, async = false)
            }})}
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

        users.current.wantsJoke = false

        if (users.count > 1 && users.other.wantsJoke == true) {
            furhat.attend(users.other)

            random(
                {furhat.ask("How about you ${users.current.name}? Do you want to hear a joke?")},
                {furhat.ask("How about you my friend?")},
                {furhat.ask("How about you ${users.current.name}? Wanna hear a joke?")}
            )
        } else {
            goto(Idle)
        }
    }
}
