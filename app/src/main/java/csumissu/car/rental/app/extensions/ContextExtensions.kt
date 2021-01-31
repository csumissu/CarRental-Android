package csumissu.car.rental.app.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import csumissu.car.rental.app.R

fun Context.inflate(@LayoutRes res: Int, parent: ViewGroup? = null, attach: Boolean = false): View {
    return LayoutInflater.from(this).inflate(res, parent, attach)
}

fun Context.drawable(@DrawableRes res: Int): Drawable {
    return ResourcesCompat.getDrawable(resources, res, theme)!!
}