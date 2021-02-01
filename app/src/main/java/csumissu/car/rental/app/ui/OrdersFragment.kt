package csumissu.car.rental.app.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import csumissu.car.rental.app.R
import csumissu.car.rental.app.extensions.drawable
import csumissu.car.rental.app.repository.CarRentalRepository
import csumissu.car.rental.app.ui.adapter.OrderAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class OrdersFragment : Fragment(R.layout.fragment_orders), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mSwipeContainer: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mOrderAdapter: OrderAdapter
    private val mRepository = CarRentalRepository()
    private var mDisposable: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeContainer = view.findViewById(R.id.swipe_container)
        mSwipeContainer.setOnRefreshListener(this)
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light, android.R.color.holo_red_light)

        mRecyclerView = view.findViewById(R.id.rv_order_items)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(view.context.drawable(R.drawable.list_item_divider))
        mRecyclerView.addItemDecoration(itemDecoration)

        mOrderAdapter = OrderAdapter { println(it) }
        mRecyclerView.adapter = mOrderAdapter
    }

    override fun onStart() {
        super.onStart()
        mSwipeContainer.post {
            mSwipeContainer.isRefreshing = true
            onRefresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDisposable?.dispose()
    }

    override fun onRefresh() {
        mDisposable = mRepository.searchOrders()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { mSwipeContainer.isRefreshing = false }
                .subscribe({
                    mOrderAdapter.setData(it)
                }, {
                    Log.e("OrderFragment", it.message ?: "", it)
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                })
    }
}