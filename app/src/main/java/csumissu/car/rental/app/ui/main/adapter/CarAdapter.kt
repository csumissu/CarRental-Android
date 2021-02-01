package csumissu.car.rental.app.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import csumissu.car.rental.app.R
import csumissu.car.rental.app.extensions.inflate
import csumissu.car.rental.app.repository.Car
import kotlinx.android.synthetic.main.item_car.view.*

class CarAdapter(private val mItemClick: ((Car) -> Unit))
    : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    private val mCarList = mutableListOf<Car>()

    fun setData(list: List<Car>?) {
        mCarList.clear()
        list?.let { mCarList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.context.inflate(R.layout.item_car, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mCarList[position].let { car ->
            holder.bind(car)
            holder.itemView.setOnClickListener { mItemClick.invoke(car) }
        }
    }

    override fun getItemCount(): Int {
        return mCarList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(car: Car) = with(car) {
            itemView.car_id.text = String.format("#%d", id)
            itemView.car_model.text = model
            itemView.car_price.text = String.format("$%s per day", dailyRentPrice.toPlainString())
        }
    }
}