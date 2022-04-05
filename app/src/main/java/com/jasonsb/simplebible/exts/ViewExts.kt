package com.jasonsb.simplebible.exts

import android.text.method.LinkMovementMethod
import android.widget.TextView


fun TextView.makeClickable() {
    movementMethod = LinkMovementMethod()
}