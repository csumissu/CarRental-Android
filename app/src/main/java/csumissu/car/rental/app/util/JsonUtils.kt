package csumissu.car.rental.app.util

import com.google.gson.*
import java.lang.reflect.Type
import java.time.Instant

object JsonUtils {

    val DEFAULT_GSON: Gson by lazy {
        GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Instant::class.java, object : JsonDeserializer<Instant> {
                    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Instant {
                        return Instant.parse(json.asJsonPrimitive.asString)
                    }
                })
                .create()
    }

    inline fun <reified T : Any> convertFromJson(json: String?): T? =
            DEFAULT_GSON.fromJson(json, T::class.java)

}

