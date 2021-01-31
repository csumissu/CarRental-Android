package csumissu.car.rental.app.repository

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class CarRentalRepository {

    private val mApiService = CarRentalApi.getApiService()

    fun searchCars(): Flowable<List<Car>> {
        return mApiService.searchCars()
                .subscribeOn(Schedulers.io())
                .map { it.data?.content }
    }

}