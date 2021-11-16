package furhatos.app.jokebot

import furhatos.flow.kotlin.UserDataDelegate
import furhatos.records.User

var User.name : String? by UserDataDelegate()
