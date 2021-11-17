package furhatos.app.joke

import furhatos.app.joke.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class JokeSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
