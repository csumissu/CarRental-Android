package csumissu.car.rental.app.repository

import csumissu.car.rental.app.BuildConfig
import csumissu.car.rental.app.util.JsonUtils
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CarRentalApi {

    @GET("cars")
    fun searchCars(): Flowable<ResponseResult<Page<Car>>>

    companion object {

        private val API_HOST = "http://fb531c32694d.ngrok.io/"

        fun getApiService(): CarRentalApi {
            val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
                    .addInterceptor(getHttpLoggingInterceptor())
                    .retryOnConnectionFailure(true)

            return Retrofit.Builder()
                    .baseUrl(API_HOST)
                    .client(clientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(JsonUtils.DEFAULT_GSON))
                    .build()
                    .create(CarRentalApi::class.java)
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
            return logInterceptor
        }

    }

}