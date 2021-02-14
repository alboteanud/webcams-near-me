package com.craiovadata.webcams

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.craiovadata.webcams.model.Content
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class UiUtil {

    companion object {

        fun ImageView.loadWebcamImage(url: String) {
            //        itemView.titleWebcam.text = " "
//        val mapImageView = itemView.webcamImageView
//            val requestOptions = RequestOptions()
//                .placeholder(R.drawable.ic_image)
//                .error(R.drawable.ic_image)

            Glide.with(this)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
//                    itemView.titleWebcam.text =
//                        itemView.resources.getString(R.string.camera_preview_error)
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
//                      val time = CityData.getFormatterCityTZ("yyyy-MM-dd HH:mm a zzzz").format(webcam.update)
//                      itemView.titleWebcam.text ="${webcam.title }\n$time"
//                    itemView.titleWebcam.text = webcam.title
                        return false
                    }
                })
//                .apply(requestOptions)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(this)
        }

        fun log(s: String) {
            if (BuildConfig.DEBUG) Timber.d(s)
        }


        fun buildAlertMessageNoGps(context: Context) {
            val builder = AlertDialog.Builder(context);
            builder.setMessage("Your GPS is disabled. Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel(); }
            val alert = builder.create();
            alert.show();
        }

//        fun snack(message: String) {
//            val view = findViewById(R.id.root) as? View
//            view?.let {
//                Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
//            }
//        }

    }
}