package furhatos.app.jokebot.jokes

/**
 * Returns a comment on the joke, based on joke score. This could be
 * -> A new joke
 * -> A mixed reception joke
 * -> A positive reception joke
 * -> A negative reception joke.
 */
fun getJokeComment(score: Double?): String {
    return when {
        score == null -> newIntros.random()
        score > 0.0 -> positiveIntros.random()
        score < 0.0 -> negativeIntros.random()
        else -> neutralIntros.random()
    }
}

private val newIntros = listOf(
        "I haven't tried this one before actually.",
        "This is a new one.",
        "I picked up this one just recently.",
        "This is a new one. Let's see."
)

private val neutralIntros = listOf(
        "Let’s see what you think of this one.",
        "Mixed reactions on this one so far.",
        "Some people like this one.",
        "I am curious what you think of this one."
)

private val positiveIntros = listOf(
        "People usually like this one.",
        "This one has been a success so far.",
        "This is a good one.",
        "I love this one."
)

private val negativeIntros = listOf(
        "This one is not liked by many, but maybe it's your kind of humor.",
        "I haven’t had much luck with this one.",
        "I’ll give this one another chance",
        "This one is quite controversial. Let me see what you think."
)

/**
 * Gives a comment based on the users reaction to the joke.
 *  -> User smiled or not
 *  -> User said it was a good/bad joke.
 */
fun getResponseOnUser(didUserLaugh: Boolean, badJoke: Boolean, goodJoke: Boolean): String {
    return if (didUserLaugh) {
        when {
            badJoke -> negativeCommentSmileResponse.random()
            goodJoke -> positiveCommentSmileResponse.random()
            else -> smileResponse.random()
        }
    } else {
        when {
            badJoke -> negativeCommentNoSmileResponse.random()
            goodJoke -> positiveCommentNoSmileResponse.random()
            else -> noSmileResponse.random()
        }
    }
}

private val negativeCommentSmileResponse = listOf(
        "At least it made you smile.",
        "Hey you smiled, so it can't have been that bad.",
        "At least this one made you smile.",
        "Hey you smiled. I guess it is not that bad."
)

private val negativeCommentNoSmileResponse = listOf(
        "I see that you didn't smile, so this is not your kind of joke, noted.",
        "Terrible joke, I am sorry. I see that you did not even smile.",
        "I see that you did not even smile. This seems not to be your kind of humor, sorry.",
        "Ouch, I guess I need to work on that one a bit more, such that I can make you smile next time"
)

private val positiveCommentNoSmileResponse = listOf(
        "But it didn't make you smile.",
        "I'm not sure if you're being sarcastic right now. You did not even smile.",
        "Hmm, your face shows a different reaction, at least it didn't make you smile.",
        "But you didn't smile."
)

private val positiveCommentSmileResponse = listOf(
        "Wow, this made you smile! You really liked this joke!",
        "This is a very enjoyable joke, I can see it on your face.",
        "I can see on your face that we have the same sense of humor, amazing!",
        "Glad that you liked this one! Love to see you smile."
)

private val noSmileResponse = listOf(
        "Not that funny? Noted. Next time I try to make you smile.",
        "No? Alright. I try to find a joke that makes you smile next time.",
        "Not your style? I see ...",
        "You didn't like that one? No smile at all? Okay."
)

private val smileResponse = listOf(
        "I'm glad that this one made you smile.",
        "Not bad right? At least it made you smile.",
        "Yay, I'm getting the hang of this, I already made you smile.",
        "Happy to see you smile."
)

val continuePrompt = listOf(
        "Should I go on?",
        "Would you like another one?",
        "How about another one?",
        "Should I give it another try?"
)