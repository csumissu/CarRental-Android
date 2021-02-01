package csumissu.car.rental.app.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import csumissu.car.rental.app.R
import csumissu.car.rental.app.repository.CarRentalRepository
import csumissu.car.rental.app.repository.OrderDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class OrderDetailActivity : AppCompatActivity() {

    private val mRepository = CarRentalRepository()
    private val mDisposables = mutableListOf<Disposable>()

    private val mCloseButton by lazy { findViewById<Button>(R.id.close_button) }
    private val mCarModelView by lazy { findViewById<TextView>(R.id.car_model) }
    private val mDailyPricelView by lazy { findViewById<TextView>(R.id.daily_price) }
    private val mBookedDaysView by lazy { findViewById<TextView>(R.id.book_days) }
    private val mTotalPriceView by lazy { findViewById<TextView>(R.id.total_price) }
    private val mBookedAtView by lazy { findViewById<TextView>(R.id.booked_at) }
    private val mReturnedAtView by lazy { findViewById<TextView>(R.id.returned_at) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        intent.getLongExtra(KEY_ORDER_ID, 0L).let {
            if (it <= 0) finish() else {
                title = String.format("Order #%d", it)
                fetchOrderFromRepository(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
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
        mCarModelView.text = orderDetail.car.model
        mDailyPricelView.text = String.format("$%s", orderDetail.dailyRentPrice.toPlainString())
        mBookedDaysView.text = orderDetail.bookedDays.toString()
        mTotalPriceView.text = String.format("$%s", orderDetail.totalPrice.toPlainString())

        val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault())
        mBookedAtView.text = dateTimeFormatter.format(orderDetail.bookedAt)
        mReturnedAtView.text = orderDetail.returnedAt?.let { dateTimeFormatter.format(it) } ?: "-"

        mCloseButton.visibility = if (orderDetail.returnedAt == null) View.VISIBLE else View.GONE
        mCloseButton.setOnClickListener { onCloseButtonClicked(orderDetail.id) }
    }

    private fun onCloseButtonClicked(id: Long) {
        val disposable = mRepository.closeOrder(id)
                .doOnSubscribe { mCloseButton.isClickable = false }
                .doOnTerminate { mCloseButton.isClickable = true }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    finish()
                }, {
                    Log.e(TAG, it.message, it)
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                })
        mDisposables.add(disposable)
    }

    companion object {
        const val KEY_ORDER_ID = "order-id"
        private const val TAG = "OrderDetailActivity"
    }

}