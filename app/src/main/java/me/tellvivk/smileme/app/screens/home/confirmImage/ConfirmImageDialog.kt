package me.tellvivk.smileme.app.screens.home.confirmImage

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.layout_confirm_image_dialog.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.helpers.imageHelper.ImageHelperI
import java.io.File

class ConfirmNewImageDialog(context: Context,
                            private val bitmap: Bitmap?,
                            private val imagePath: String?,
                            private val imageHelper: ImageHelperI,
                            private val callback: ConfirmNewImageDialogInterface):
    Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_confirm_image_dialog)
        setCancelable(false)

        imagePath?.let {
            imageHelper.loadFromFile(context = context,
                file = File(imagePath), iv = imgConfirmImage)
        }

        btnConfirmImageCancel.setOnClickListener {
            dismiss()
        }

        btnConfirmImageOkay.setOnClickListener {

            if (txtConfirmImageTitle.text.isNullOrEmpty() ||
                txtConfirmImageTitle.text.isNullOrBlank()){
                txtConfirmImageTitle.error = "Please enter a title"
            } else{
                callback.confirmed(
                    title = txtConfirmImageTitle.text.toString(),
                    comment = txtConfirmImageComment.text.toString()
                )
                dismiss()
            }
        }
    }

}

interface ConfirmNewImageDialogInterface{
    fun confirmed(title: String, comment: String)
}