package com.alvaroquintana.calculadoraperruna.ui.helpers

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.alvaroquintana.calculadoraperruna.R
import com.alvaroquintana.calculadoraperruna.ui.components.AspectRatioImageView
import com.alvaroquintana.calculadoraperruna.utils.glideLoadBase64

class ImagePreviewer {

    fun show(context: Context, source: ImageView, icon: String) {
        val background: BitmapDrawable = ImagePreviewerUtils.getBlurredScreenDrawable(context, source.rootView)
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.view_image_previewer, null)

        val imageView: AspectRatioImageView = dialogView.findViewById(R.id.imageBreed) as AspectRatioImageView
        glideLoadBase64(context, icon, imageView)

        val dialog = Dialog(context, R.style.ImagePreviewerTheme)
        dialog.window?.setBackgroundDrawable(background)
        dialog.setContentView(dialogView)
        dialog.show()

        dialogView.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (dialog.isShowing) {
                    dialog.dismiss()
                    return true
                }
                return false
            }
        })
    }
}