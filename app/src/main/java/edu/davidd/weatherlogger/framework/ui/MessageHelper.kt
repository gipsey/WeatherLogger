package edu.davidd.weatherlogger.framework.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showMessage(uiMessage: UiMessage) =
    Snackbar.make(
        this,
        uiMessage.resId,
        Snackbar.LENGTH_LONG
    ).run {
        if (uiMessage.action != null)
            setAction(uiMessage.action.actionResId) {
                uiMessage.action.runnable.invoke(it.context)
            }

        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.maxLines = 5

        show()
    }

class UiMessage(@StringRes val resId: Int, val action: UiMessageAction? = null)

class UiMessageAction(
    @StringRes val actionResId: Int,
    val runnable: (Context) -> Unit
)