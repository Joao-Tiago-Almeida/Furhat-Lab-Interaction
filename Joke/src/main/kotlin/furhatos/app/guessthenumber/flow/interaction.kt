package furhatos.app.guessthenumber.flow

import furhatos.app.guessthenumber.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import java.util.jar.Attributes
import javax.naming.Name

public val name_map = hashMapOf<String, String>()

val Start : State = state(Interaction) {

    onEntry {
        furhat.say("hej")//Welcome, please give make answers as short and simple as you can!
        furhat.gesture(Gestures.Smile(duration=1.5))
        random(
            {   furhat.ask("Your name is ...") },
            {   furhat.ask("How do you like to me called?")    },
            {   furhat.ask("How can I call you?")    }
        )
    }

    onResponse { // Catches everything else
        val name = it.text
        name_map[it.userId] = name
        furhat.say("Nice to meet you $name, you are the user number ${it.userId}")
        goto(Idle)
    }
}