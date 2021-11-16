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

    onResponse<TellName>{
        users.current.name = "${it.intent.name}"
        random(
            {furhat.say(utterance {+"${users.current.name}, what a wonderful name"
                                  + Gestures.Smile})},
            {furhat.say(utterance {+"${users.current.name}... I like that name!"
                                   + Gestures.Wink})}
        )
        goto(AreYouHappy)
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
        furhat.say("Great to hear, then you are in the right mood!")
        goto(RequestJokeTest)
    }

    onResponse<No> {
        furhat.say("I'm sorry to hear that. Hmm, perhaps we can do something to cheer you up.")
        goto(RequestJokeTest)
    }

    onResponse {
        furhat.say("Perhaps we can try to increase your happiness a few notches.")
        goto(RequestJokeTest)
    }
}

val RequestJokeTest: State = state(Interaction) {
    onEntry {
        furhat.ask("I’m trying to learn some humor you see. So. Could I test a few jokes on you?")
    }

    onResponse<Yes> {
        furhat.say("Awesome")
        goto(JokeSequence)
    }

    onResponse<No> {
        furhat.say("Oh, that’s a shame.")
        goto(Idle)
    }
}
