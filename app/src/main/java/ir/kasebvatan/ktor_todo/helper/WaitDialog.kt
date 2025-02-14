package ir.kasebvatan.ktor_todo.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.Window
import ir.kasebvatan.ktor_todo.R

object WaitDialog {

    var dialog: Dialog? = null
    fun show(context: Context?) {
        dialog = Dialog(context!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_wait)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        //dialog!!.window!!.setWindowAnimations(R.style.dialogAnimation)
        dialog!!.setCancelable(true)

        try {
            dialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun hide() {
        try {
            if (dialog != null) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }
}