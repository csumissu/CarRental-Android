package csumissu.car.rental.app.repository

import java.math.BigDecimal

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