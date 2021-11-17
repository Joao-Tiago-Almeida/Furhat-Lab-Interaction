package furhatos.app.joke.flow

import furhatos.flow.kotlin.*
import furhatos.util.*

val Idle: State = state {

    init {
        furhat.attendAll()
        furhat.say("welcome!! I am a robot, r u also!? ahah joking ... please can yall introduce yourselves?")
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        if (name_map.size < 2 && users.count == 2) {
            furhat.attend(users.other)
            goto(Start)
        }
        else
            furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }

    onUserLeave {
        name_map.remove(it.id)
    }
}

val Interaction: State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }

}