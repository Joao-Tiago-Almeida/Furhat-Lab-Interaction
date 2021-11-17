package furhatos.app.jokebot.nlu

import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.util.Language

class GoodJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Good one", "I like it", "good joke", "very funny", "hilarious", "haha", "great joke", "amazing", "haha", "hahaha", "hahahaha", "love that", "great joke"
        )
    }
}

class BadJoke: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "not funny", "not that funny", "not good", "not so good", "bad joke", "terrible", "bad", "oh no", "no"
        )
    }
}

class Name : EnumEntity(stemming = false, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf(
                "Joao", "Victor", "Philipp", "Katie", "Manuel", "Alex", "Sofia", "Olivia", "Liam", "Emma", "Noah", "Amelia",
                "Oliver", "Sophia", "Lucas", "Charlotte", "Levi", "James", "Dennis","Elsa", "Marcel-Robert", "Iolanda", "Thomas", "Manuel", "Arzu", "Emil", "Laura",
                "Edlidir", "Ola", "João", "Philip", "Kristin", "Isak", "Divya", "Alexander", "Mikael", "Miklovana", "Katie", "Elmira", "Ilaria")
    }
}

class TellNameBriefly(val name : Name? = null): Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "@name", "I am @name")
    }
}