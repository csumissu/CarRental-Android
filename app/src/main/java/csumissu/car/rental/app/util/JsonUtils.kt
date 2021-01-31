package csumissu.car.rental.app.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object JsonUtils {

    val DEFAULT_GSON: Gson by lazy {
        GsonBuilder()
                .serializeNulls()
                .create()
    }

    inline fun <reified T : Any> convertFromJson(json: String?): T? =
            DEFAULT_GSON.fromJson(json, T::class.java)

}

