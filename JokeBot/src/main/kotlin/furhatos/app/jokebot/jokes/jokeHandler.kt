package furhatos.app.jokebot.jokes

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import furhatos.util.CommonUtils

val BASIC_LIST_OF_JOKES = listOf(
        Joke("What do robots eat as snacks", "micro-chips"),
        Joke("What do robots do at lunchtime", "have a mega-byte"),
        Joke("Why was the robot bankrupt?", "He had used all his cache"),
        Joke("Why do robots take holidays?", "To recharge their batteries"),
        Joke("Why was the robot tired when it got home?", "it had a hard drive!"),
        Joke("What do you call a robot who always runs into the wall?", "Wall-E", 1.0),
        Joke("Why do you have to be careful with robot dogs?", "They byte"),
        Joke("What happens to robots after they go defunct?", "They rust in peace"),
        Joke("Why did the robot fall in love with the magnet", "He couldn't resist the attraction"),
        Joke("How do robots pay for things?", "With cache, of course!"),
        Joke("I bought a wooden computer, guess what?", "It wooden work!"),
        Joke("Why did the robot sneeze?", "It had a virus"),
        Joke("What do you get if you cross a robot with a tractor?", "A trans-farmer"),
        Joke("What do you get if you cross a robot with a rock band?", "Heavy metal")
)

data class Joke(val intro: String, val punchline: String, var score: Double? = null)


object JokeHandler {

    /**
     * File to read/save jokes to.
     * Can be found in the user's home directory/.furhat/jokes.json when running on SDK.
     */
    private val jokeFile = CommonUtils.getAppDataDir("jokes.json")
    private val gson = Gson().newBuilder().setPrettyPrinting().create() //JSON parser/serializer
    private val listOfJokes = getJokes() //List of jokes
    private lateinit var currentJoke: Joke //Will be initialized when we request the first joke.

    /**
     * Returns a joke, and saves that joke to the current joke variable.
     */
    fun getJoke(): Joke {
        currentJoke = listOfJokes.random()
        return currentJoke
    }

    /**
     * Changes the current joke score with the change provided.
     */
    fun changeJokeScore(scoreChange: Double) {
        if (currentJoke.score == null) {
            currentJoke.score = 0.0
        }
        currentJoke.score = currentJoke.score!! + scoreChange
        writeToFile()
    }

    /**
     * Tries to read the jokes from file. If that file does not exist, or the JSON cannot be parsed, we return a basic
     * list of jokes.
     */
    private fun getJokes(): List<Joke> {
        return if (jokeFile.exists()) {
             try {
                 gson.fromJson(jokeFile.readText(), Array<Joke>::class.java).toList()
            } catch (_: JsonSyntaxException) {
                BASIC_LIST_OF_JOKES
            }
        } else {
            BASIC_LIST_OF_JOKES
        }
    }

    /**
     * Writes the current list of jokes to a file
     */
    private fun writeToFile() {
        if (!jokeFile.exists()) {
            jokeFile.createNewFile()
        }
        jokeFile.writeText(gson.toJson(listOfJokes))
    }

}
