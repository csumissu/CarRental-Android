package csumissu.car.rental.app.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import csumissu.car.rental.app.repository.CarRentalRepository
import csumissu.car.rental.app.repository.OrderDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class OrderDetailActivity : AppCompatActivity() {

    private val mRepository = CarRentalRepository()
    private val mDisposables = mutableListOf<Disposable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getLongExtra(KEY_ORDER_ID, 0L).let {
            if (it <= 0) finish() else {
                title = String.format("Order #%d", it)
                fetchOrderFromRepository(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.forEach { it.dispose() }
    }

    private fun fetchOrderFromRepository(id: Long) {
        val disposable = mRepository.getOrder(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    initViews(it)
                }, {
                    Log.e(TAG, it.message, it)
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                })
        mDisposables.add(disposable)
    }

    private fun initViews(orderDetail: OrderDetail) {
        println(orderDetail)
    }

    companion object {
        const val KEY_ORDER_ID = "order-id"
        private const val TAG = "OrderDetailActivity"
    }

}