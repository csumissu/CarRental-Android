package csumissu.car.rental.app.repository

import csumissu.car.rental.app.BuildConfig
import csumissu.car.rental.app.util.JsonUtils
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CarRentalApi {

    @GET("cars")
    fun searchCars(@Query("status") status: Int): Flowable<ResponseResult<Page<Car>>>

    @GET("orders")
    fun searchOrders(@Query("sort") sort: List<String>): Flowable<ResponseResult<Page<Order>>>

    @POST("orders")
    fun createOrder(@Body request: CreateOrderRequest): Flowable<ResponseResult<CreateOrderResponse>>

    @GET("orders/{id}")
    fun getOrder(@Path("id") id: Long): Flowable<ResponseResult<OrderDetail>>

    @POST("orders/{id}/close")
    fun closeOrder(@Path("id") id: Long): Flowable<ResponseResult<Unit>>

    companion object {

        private const val API_HOST = "http://bc.csumissu.xyz/"

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