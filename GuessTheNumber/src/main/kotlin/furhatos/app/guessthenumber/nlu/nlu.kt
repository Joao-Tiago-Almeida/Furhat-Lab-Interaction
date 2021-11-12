package furhatos.app.guessthenumber.nlu

import furhatos.nlu.Intent
import furhatos.util.Language
import furhatos.nlu.*
import furhatos.nlu.grammar.Grammar
import furhatos.nlu.kotlin.grammar
import furhatos.nlu.common.Number


class GetName(
    val name: String? = null
) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@name",
            "My name is @name",
            "I am @name",
            "You are speaking with @name"
        )
    }
}