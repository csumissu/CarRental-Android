package csumissu.car.rental.app.repository

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class CarRentalRepository {

    private val mApiService = CarRentalApi.getApiService()

    fun searchCars(): Flowable<List<Car>> {
        return mApiService.searchCars(STATUS_AVAILABLE)
                .subscribeOn(Schedulers.io())
                .map { it.data?.content }
    }

    fun searchOrders(): Flowable<List<Order>> {
        return mApiService.searchOrders("bookedAt,returnedAt")
                .subscribeOn(Schedulers.io())
                .map { it.data?.content }
    }

    fun createOrder(carId: Long, bookedDays: Int): Flowable<Long> {
        return mApiService.createOrder(CreateOrderRequest(carId, bookedDays))
                .subscribeOn(Schedulers.io())
                .map { it.data?.id }
    }

    companion object {
        private const val STATUS_AVAILABLE = 1;
    }

}