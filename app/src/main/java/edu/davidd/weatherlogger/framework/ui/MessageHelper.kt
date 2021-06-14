package edu.davidd.weatherlogger.framework.ui

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showMessage(
    @StringRes messageResId: Int,
    action: MessageAction? = null
) =
    Snackbar.make(
        this,
        messageResId,
        Snackbar.LENGTH_LONG
    ).run {
        if (action != null)
            setAction(action.actionResId, action.runnable)

        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.maxLines = 5

        show()
    }

class MessageAction(
    @StringRes val actionResId: Int,
    val runnable: (View) -> Unit
)