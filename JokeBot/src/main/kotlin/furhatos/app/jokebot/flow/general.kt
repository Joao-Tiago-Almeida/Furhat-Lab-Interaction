package furhatos.app.jokebot.flow

import furhatos.app.jokebot.jokes.indefiniteBigSmile
import furhatos.app.jokebot.jokes.indefiniteSmile
import furhatos.app.jokebot.jokes.stopSmile
import furhatos.app.jokebot.name
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.gestures.Gestures
import furhatos.nlu.common.TellName
import furhatos.skills.emotions.UserGestures
import furhatos.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val Idle: State = state {

    init {
        furhat.voice = PollyNeuralVoice.Matthew()
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}

/**
 * Parent state that smiles to the current user, if the current user is smiling.
 *
 * When a user smiles there are three possibilities.
 *  -> Furhat gives the user a big smile.
 *  -> Furhat gives the user a smile.
 *  -> Furhat does nothing.
 *
 *  If Furhat smiled, then there is a smile cooldown.
 */
val SmileBack: State = state {

    val smileProbability = 0.25 //25% of the time we do a small smile
    val bigSmileProbability = 0.5 //50% of the time we do a big smile
    val smileTimeout = 5000L //We don't want to trigger the smiling back too much, therefor after a smile we wait 5000ms
    var smilingIsAllowed = true //Boolean depicting if Furhat is allowed to smile.

    /**
     * Lambda function that starts a thread, which allows Furhat to smile again after the set smile timeout.
     */
    val resetAllowedToSmile = { GlobalScope.launch { delay(smileTimeout); smilingIsAllowed = true }}

    //Instant trigger ('parallel thread') that detects a user smiling and takes action.
    onUserGesture(UserGestures.Smile, cond = { it.isCurrentUser && smilingIsAllowed}, instant = true) {
        val randomNumber = Math.random()
        when {
            randomNumber > bigSmileProbability -> {
                smilingIsAllowed = false
                furhat.gesture(indefiniteBigSmile)
                resetAllowedToSmile()
            }
            randomNumber > smileProbability -> {
                smilingIsAllowed = false
                furhat.gesture(indefiniteSmile)
                resetAllowedToSmile()
            }
            else -> {
                //Nothing
            }
        }
    }

    onUserGestureEnd(UserGestures.Smile, cond = { it.isCurrentUser }, instant = true) {
        furhat.gesture(stopSmile)
    }
}

val Interaction: State = state(SmileBack) {

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

    onUserEnter() { // instant = true
        var mainUser = users.current

        // attend new entering user
        furhat.attend(it)
        random(
            {furhat.say("Hello there! Lovely that you joined our small meeting.")},
            {furhat.say("Hello! Nice to have more people joining today!")},
            {furhat.say("Welcome! What a pleasure to see you")}
        )
        furhat.glance(mainUser)
        furhat.attend(mainUser)

        reentry()
    }

    onResponse<TellName>{
        users.current.name = "${it.intent.name}"

        if (users.current.name == "James"){
            random(
                {furhat.say(utterance {+"Your name is also ${users.current.name}?"
                    + blocking {
                        furhat.gesture(Gestures.Surprise, async = false)
                    }
                    + "What a funny coincidence!"})},
                {furhat.say(utterance {+"What? Your name is also ${users.current.name}?"
                    + blocking {
                        furhat.gesture(Gestures.BigSmile, async = false)
                    }
                    + "How funny!"})},
                {furhat.say(utterance {+"We are both named ${users.current.name}?"
                    + blocking {
                        furhat.gesture(Gestures.Surprise, async = false)
                    }
                    + "What a funny coincidence!"})}
            )
        } else {
            random(
                {
                    furhat.say(utterance {
                        +"${users.current.name}, what a wonderful name"
                        + blocking {
                            furhat.gesture(Gestures.BigSmile, async = false)
                        }
                    })
                },
                {
                    furhat.say(utterance {
                        +"${users.current.name}... I like that name!"
                        + blocking {
                            furhat.gesture(Gestures.Wink, async = false)
                        }
                    })
                }
            )
        }
        goto(AreYouHappy)
    }

}
