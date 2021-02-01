package csumissu.car.rental.app.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.time.Instant

data class ResponseResult<T>(val code: Int,
                             val message: String,
                             val data: T?)

data class Page<T>(val content: List<T>,
                   val totalPages: Int,
                   val totalElements: Int,
                   val numberOfElements: Int)

@Parcelize
data class Car(val id: Long,
               val model: String,
               val dailyRentPrice: BigDecimal,
               val status: Int) : Parcelable

@Parcelize
data class Order(val id: Long,
                 val carId: Long,
                 val bookedAt: Instant,
                 val returnedAt: Instant?,
                 val dailyRentPrice: BigDecimal,
                 val bookedDays: Int,
                 val totalPrice: BigDecimal) : Parcelable


data class CreateOrderRequest(val carId: Long,
                              val bookedDays: Int)

data class CreateOrderResponse(val id: Long)

data class OrderDetail(
        val id: Long,
        val bookedAt: Instant,
        val returnedAt: Instant?,
        val dailyRentPrice: BigDecimal,
        val bookedDays: Int,
        val totalPrice: BigDecimal,
        val car: Car) {

    data class Car(val id: Long,
                   val model: String)
}