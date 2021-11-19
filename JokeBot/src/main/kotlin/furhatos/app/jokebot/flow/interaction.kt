package furhatos.app.jokebot.flow

import furhatos.app.jokebot.name
import furhatos.app.jokebot.wantsJoke
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import khttp.async


val Start : State = state(Interaction) {
    val name : String = "Katie"

    onEntry {
        if(users.current.name == null) {
            random(
                { furhat.ask("And what about you? What is your name?") },
                { furhat.ask("And what is your name?") },
                { furhat.ask("And your name is what?") }
            )
        }
        else {
            goto(Idle)
        }
    }

    onReentry {
        if(users.current.name == null) {
            random(
                { furhat.ask("What is your name?") },
                { furhat.ask("What was your name?") },
                { furhat.ask("Your name is what?") }
            )
        }
        else {
            goto(Idle)
        }
    }

    onNoResponse {
        if(users.current.name == null) {
            random(
                { furhat.say("Hello? Sorry, you might have missed that.")}, // I was asking for your name?") },
                { furhat.say("Hello, somebody out there?") },
                { furhat.say("Did you say something?")} // Sorry I might have missed that. What is your name again?") }
            )
            furhat.ask("My name is $name, what is yours?")
        }
    }
}

val SelfPresent : State = state{
    val name : String = "Katie"
    onEntry {
        random(
            {furhat.say("Hi there. It is a pleasure to meet you. I am $name.")},
            {furhat.say("Hello my friend. Glad to meet you! My name is $name.")},
            {furhat.say("Hi there. What a pleasure meeting you. My name is $name.")}
        )
        goto(Start)
    }
}

val AreYouHappy: State = state(Interaction) {

    onEntry {

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
        random(
            {furhat.say("But I respect that. There are certainly more funny things than a joking robot.")},
            {furhat.say("However, I understand that. There might be more funny things than a robot joking.")},
            {furhat.say("But no problem. I understand that there are more funny things than a joking robot.")}
        )

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
