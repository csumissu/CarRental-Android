package csumissu.car.rental.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import csumissu.car.rental.app.R
import csumissu.car.rental.app.repository.Car
import csumissu.car.rental.app.repository.CarRentalRepository
import csumissu.car.rental.app.ui.OrderDetailActivity.Companion.KEY_ORDER_ID
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.math.BigDecimal

class CarDetailActivity : AppCompatActivity() {

    private val mBookButton by lazy { findViewById<Button>(R.id.book_button) }
    private val mDailyPriceView by lazy { findViewById<TextView>(R.id.daily_price) }
    private val mBookDaysView by lazy { findViewById<EditText>(R.id.book_days) }
    private val mTotalPriceView by lazy { findViewById<TextView>(R.id.total_price) }

    private val mRepository = CarRentalRepository()
    private var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)
        intent.getParcelableExtra<Car>(KEY_CAR_DATA)?.also { initViews(it) } ?: finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()
    }

    private fun initViews(car: Car) {
        title = String.format("#%d %s", car.id, car.model)

        mDailyPriceView.text = String.format("$%s", car.dailyRentPrice.toPlainString())

        mBookDaysView.doAfterTextChanged {
            it?.toString()?.toIntOrNull()?.let {
                val totalPrice = car.dailyRentPrice.multiply(BigDecimal(it))
                mTotalPriceView.text = String.format("$%s", totalPrice.toPlainString())
            } ?: run {
                mTotalPriceView.text = "-"
            }
        }
        mBookDaysView.setText(R.string.book_days_min)

        mBookButton.setOnClickListener { onClickBookButton(it.context, car) }
    }

    private fun onClickBookButton(context: Context, car: Car) {
        val bookedDays = mBookDaysView.text.toString().toIntOrNull()
        if (bookedDays == null) {
            Toast.makeText(context, "Book days is required", Toast.LENGTH_SHORT).show()
            return
        }

        mDisposable = mRepository.createOrder(car.id, bookedDays)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mBookButton.isClickable = false }
                .doOnTerminate { mBookButton.isClickable = true }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val intent = Intent(context, OrderDetailActivity::class.java)
                    intent.putExtra(KEY_ORDER_ID, it)
                    startActivity(intent)
                    finish()
                }, {
                    Log.e(TAG, it.message, it)
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                })
    }

    companion object {
        const val KEY_CAR_DATA = "car"
        private const val TAG = "CarDetailActivity"
    }

}