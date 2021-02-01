package csumissu.car.rental.app.repository

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class CarRentalRepository {

    private val mApiService = CarRentalApi.getApiService()

    fun searchCars(): Flowable<List<Car>> {
        return mApiService.searchCars(STATUS_AVAILABLE)
                .subscribeOn(Schedulers.io())
                .map { if (isSuccess(it)) it.data?.content else throw ApiException(it.message) }
    }

    fun searchOrders(): Flowable<List<Order>> {
        val sorts = listOf("bookedAt,desc", "returnedAt,asc")
        return mApiService.searchOrders(sorts)
                .subscribeOn(Schedulers.io())
                .map { if (isSuccess(it)) it.data?.content else throw ApiException(it.message) }
    }

    fun createOrder(carId: Long, bookedDays: Int): Flowable<Long> {
        return mApiService.createOrder(CreateOrderRequest(carId, bookedDays))
                .subscribeOn(Schedulers.io())
                .map { if (isSuccess(it)) it.data?.id else throw ApiException(it.message) }
    }

    fun getOrder(id: Long): Flowable<OrderDetail> {
        return mApiService.getOrder(id)
                .subscribeOn(Schedulers.io())
                .map { if (isSuccess(it)) it.data else throw ApiException(it.message) }
    }

    fun closeOrder(id: Long): Flowable<Boolean> {
        return mApiService.closeOrder(id)
                .subscribeOn(Schedulers.io())
                .map { if (isSuccess(it)) true else throw ApiException(it.message) }
    }

    private fun <T> isSuccess(response: ResponseResult<T>): Boolean {
        return IntRange(200, 299).contains(response.code)
    }

    companion object {
        private const val STATUS_AVAILABLE = 1;
    }

}