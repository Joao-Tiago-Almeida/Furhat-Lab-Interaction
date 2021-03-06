package furhatos.app.jokebot.flow
// -Dfurhatos.skills.brokeraddress=127.0.0.1
import furhatos.app.jokebot.jokes.indefiniteBigSmile
import furhatos.app.jokebot.jokes.indefiniteSmile
import furhatos.app.jokebot.jokes.stopSmile
import furhatos.app.jokebot.name
import furhatos.app.jokebot.nlu.TellNameBriefly
import furhatos.app.jokebot.wantsJoke
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.gestures.Gestures
import furhatos.nlu.common.TellName
import furhatos.nlu.common.Yes
import furhatos.skills.emotions.UserGestures
import furhatos.util.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val Idle: State = state {
    init {
        furhat.voice = PollyNeuralVoice.Joanna()
        furhat.mask = "Fedora"
        furhat.attendNobody()
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(SelfPresent)
        }
    }

    onEntry {
        if (users.count > 0) {
            if (users.other.name == null) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                if (users.current.wantsJoke == true) { // what if users.other.wantsJoke?
                    furhat.attend(users.random)
                    goto(AreYouHappy)
                } else {
                    furhat.attendNobody()
                }
            }
        } else {
            random(
                {
                    furhat.say(utterance {
                        +blocking {
                            furhat.gesture(Gestures.ExpressSad, async = false)
                        }
                        +"Seems like I am a lonely robot!"
                    })
                },
                {
                    furhat.say(utterance {
                        +blocking {
                            furhat.gesture(Gestures.ExpressSad, async = false)
                        }
                        +"Seems like I am all by myself."
                    })
                },
                {
                    furhat.say(utterance {
                        +"Seems like I am a lonely robot."
                        +blocking {
                            furhat.gesture(Gestures.ExpressSad, async = false)
                        }
                        +"Any human out there?"
                    })
                }
            )

            furhat.attendNobody()
        }
    }

    onUserEnter {
        furhat.attend(it)
        goto(SelfPresent)
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
    val robotName : String = "Katie"
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
            {furhat.say("Welcome! What a pleasure to see you.")}
        )
        furhat.glance(mainUser)
        furhat.attend(mainUser)

        reentry()
    }

    onResponse<TellNameBriefly> {
        users.current.name = "${it.intent.name}"
        users.current.wantsJoke = true

        if (users.current.name == robotName){
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
        goto(Idle)
    }

    onResponse<TellName>{
        users.current.name = "${it.intent.name}"
        users.current.wantsJoke = true

        if (users.current.name == robotName){
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
        goto(Idle)
    }

    onResponse {

        if (users.current.name == null) {
            random(
                {furhat.say(utterance {
                +"Did you say:"
                +delay(200)
                +it.text
                +"?"
                })},
                {furhat.say(utterance {
                    +"I heard:"
                    +delay(200)
                    +it.text
                    +"?"
                })}
            )

            val validate: Boolean? = furhat.askYN(utterance {
                +delay(200)
                +"Is that your name?"
            })

            if (validate == true) {
                users.current.name = it.text
                users.current.wantsJoke = true
                random(
                    {
                        furhat.say(utterance {
                            +"${users.current.name}, what a wonderful name I just learned."
                            +blocking {
                                furhat.gesture(Gestures.BigSmile, async = false)
                            }
                        })
                    },
                    {
                        furhat.say(utterance {
                            +"${users.current.name}... Ohh one more beautiful name I learned."
                            +blocking {
                                furhat.gesture(Gestures.Wink, async = false)
                            }
                        })
                    }
                )
            }
            else{
                random(
                    {furhat.ask("So what is your name?")},
                    {furhat.ask("Okay, could you repeat your name then?")},
                    {furhat.ask("Oh sorry, so what is your name?")}
                )
                //furhat.attend(users.other)
            }
            goto(Idle)
        }
    }

    /*
    onNoResponse {
        if (users.count == 0){
            goto(Idle)
        }
        else {
            random(
                {furhat.say("Hello? Did you here me?")},
                {furhat.say("Hello? Are you there?")},
                {furhat.say("Hello? Did you hear what I said?")}
            )
            reentry()
        }
    }
    */

}
