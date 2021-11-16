package furhatos.app.guessthenumber

import furhatos.app.guessthenumber.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class GuessthenumberSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
