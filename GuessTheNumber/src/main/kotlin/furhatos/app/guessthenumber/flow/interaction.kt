package furhatos.app.guessthenumber.flow

import furhatos.app.guessthenumber.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import java.util.jar.Attributes
import javax.naming.Name

val Start : State = state(Interaction) {

    onEntry {
        furhat.say("Hello, I am a furhat Robot.")
        furhat.gesture(Gestures.Smile(duration=1.5))
        random(
            {   furhat.ask("With you I am speaking with?")  },
            {   furhat.ask("What is your name?")    }
        )
    }

    onResponse<GetName> {
        val name :String? = it.text
        if (name != null ) {
            furhat.say("Nice to meet you $name")
        }
        else {
            furhat.say("Sorry, I did not understand.")
            goto(Idle)
        }
    }

    onResponse<Yes>{
        furhat.say("I like humans.")
    }

    onResponse<No>{
        furhat.say("That's sad.")
    }
}