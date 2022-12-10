package com.nszalas.timefulness.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.setup(textObserver: (String?) -> Unit) {
    placeCursorToEnd()
    setSafeAfterTextChangedListener { text ->
        textObserver(text.takeIf { it.isNotEmpty() })
    }
}

fun EditText.placeCursorToEnd() {
    text?.let { text -> setSelection(text.length) }
}

inline fun EditText.setSafeAfterTextChangedListener(crossinline block: (String) -> Unit) {
    val listener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // no op
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // no op
        }

        override fun afterTextChanged(p0: Editable?) {
            block(p0?.toString().orEmpty())
        }
    }
    addTextChangedListener(listener)
}