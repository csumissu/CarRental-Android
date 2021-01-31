package csumissu.car.rental.app.repository

import java.math.BigDecimal
import java.time.Instant

data class ResponseResult<T>(val code: Int,
                             val message: String,
                             val data: T?)

data class Page<T>(val content: List<T>,
                   val totalPages: Int,
                   val totalElements: Int,
                   val numberOfElements: Int)

data class Car(val id: Long,
               val model: String,
               val dailyRentPrice: BigDecimal,
               val status: Int)

data class Order(val id: Long,
                 val carId: Long,
                 val bookedAt: Instant,
                 val returnedAt: Instant?,
                 val dailyRentPrice: BigDecimal,
                 val bookedDays: Int,
                 val totalPrice: BigDecimal)