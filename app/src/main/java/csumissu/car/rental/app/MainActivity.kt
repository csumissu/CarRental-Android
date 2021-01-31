package csumissu.car.rental.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val mNavView by lazy { findViewById<BottomNavigationView>(R.id.nav_view) }
    private val mNavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNavView.setupWithNavController(mNavController)
    }

}
