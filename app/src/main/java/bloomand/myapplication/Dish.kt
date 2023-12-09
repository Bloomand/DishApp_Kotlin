package bloomand.myapplication

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.random.Random

data class Dish (
    val id: Int,
    val name: String,
    val calorie: Int,
    val time: Int,
    val ingredient: String,
    val difficulty: Int,
    val imageID: Int,
    )

lateinit var dishes: Array<Dish>

fun fetchDishData(): Thread {
    return Thread {
        val url = URL("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/recipes2022.json")
        val connection = url.openConnection() as HttpsURLConnection

        if (connection.responseCode == 200) {
            val inputSystem = connection.inputStream
            val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")

            val gson = GsonBuilder()
                .registerTypeAdapter(
                    Array<Dish>::class.java,
                    JsonDeserializer {
                            json, _, _ ->
                        val jsonArray = json.asJsonArray
                        val dishArray = jsonArray.map {
                            it.asJsonObject.run {
                                Dish(0,
                                    get("Name")?.asString ?: "",
                                    get("Calorie")?.asInt ?: 0,
                                    get("Time")?.asInt ?: 0,
                                    get("Ingredients")?.asString ?: "",
                                    get("Difficulty")?.asInt ?: 0,
                                    Random.nextInt(0, 4) ?: 0
                                    )
                            }
                        }.toTypedArray()
                        return@JsonDeserializer dishArray
                    }
                )
                .create()

            dishes = gson.fromJson(inputStreamReader, Array<Dish>::class.java)

            inputStreamReader.close()
            inputSystem.close()
        } else {
            println("Failed Connection")
        }
    }
}