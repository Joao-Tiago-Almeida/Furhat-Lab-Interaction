package furhatos.app.jokebot.nlu

import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.util.Language

/*
class Name : EnumEntity(stemming = true, speechRecPhrases = true){
    override fun getEnum(lang: Language): List<String> {
        return listOf("James", "Katie", "Philipp", "Joao", "Viktor", "Manuel", "Sarah", "Emelie")
    }
}
 */

class GoodJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Good one", "I like it", "good joke", "very funny", "hilarious", "haha", "great joke", "amazing"
        )
    }
}

class BadJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "not funny", "not that funny", "not good", "not so good", "bad joke", "terrible"
        )
    }
}

/*
class TellName(val name: Name? = null): Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "My name is @name", "I am @name", "I am called @name", "Hello. My name is @name", "Hi, I am @name"
        )
    }
}
*/