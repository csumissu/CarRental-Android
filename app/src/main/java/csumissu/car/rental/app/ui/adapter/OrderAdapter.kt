package csumissu.car.rental.app.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import csumissu.car.rental.app.R
import csumissu.car.rental.app.extensions.inflate
import csumissu.car.rental.app.repository.Order
import kotlinx.android.synthetic.main.item_order.view.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class OrderAdapter(private val mItemClick: ((Order) -> Unit))
    : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private val mOrderList = mutableListOf<Order>()

    fun setData(list: List<Order>?) {
        mOrderList.clear()
        list?.let { mOrderList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.context.inflate(R.layout.item_order, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mOrderList[position].let { order ->
            holder.bind(order)
            holder.itemView.setOnClickListener { mItemClick.invoke(order) }
        }
    }

    override fun getItemCount(): Int {
        return mOrderList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val mDateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault())

        fun bind(order: Order) = with(order) {
            itemView.order_id.text = String.format("#%d", id)

            itemView.order_booked_at.text = String.format("Booked at:  %s",
                    mDateTimeFormatter.format(bookedAt))
            itemView.order_returned_at.text = String.format("Returned at:  %s",
                    returnedAt?.let { mDateTimeFormatter.format(it) } ?: "-")
            itemView.order_price.text = String.format("$%s", totalPrice.toPlainString())
        }
    }
}