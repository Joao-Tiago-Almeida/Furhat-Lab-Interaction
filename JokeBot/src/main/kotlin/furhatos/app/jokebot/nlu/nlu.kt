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