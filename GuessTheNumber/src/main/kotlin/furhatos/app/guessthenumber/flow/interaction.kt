package furhatos.app.guessthenumber.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Start : State = state(Interaction) {

    onEntry {
        furhat.say("Hello, I am a furhat Robot.")
        furhat.gesture(Gestures.Smile(duration=1.5))
        random(
            {   furhat.ask("With you I am speaking with?")  },
            {   furhat.ask("What is your name?")    }
        )
    }

    onResponse<Yes>{
        furhat.say("I like humans.")
    }

    onResponse<No>{
        furhat.say("That's sad.")
    }
}
