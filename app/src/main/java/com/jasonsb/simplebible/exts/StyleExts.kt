package com.jasonsb.simplebible.exts

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.ColorInt

fun SpannableStringBuilder.setSuperscriptSpan(
    phrase: String,
    @ColorInt color: Int,
    proportion: Float = 0.75f,
    isBold: Boolean = false,
    callback: (() -> Unit)? = null
) {
    if (callback != null) {
        setClickableSpan(phrase, callback)
    }
    setGenericSpan(SuperscriptSpan(), phrase)
    setGenericSpan(ForegroundColorSpan(color), phrase)
    setGenericSpan(RelativeSizeSpan(proportion), phrase)
    if (isBold) {
        setStyleSpan(Typeface.BOLD, phrase)
    }
}

fun SpannableStringBuilder.setItalicSpan(phrase: String) {
    setStyleSpan(Typeface.ITALIC, phrase)
}

fun SpannableStringBuilder.setBoldSpan(phrase: String) {
    setStyleSpan(Typeface.BOLD, phrase)
}

private fun SpannableStringBuilder.setStyleSpan(style: Int, phrase: String) {
    setGenericSpan(StyleSpan(style), phrase)
}

private fun SpannableStringBuilder.setClickableSpan(phrase: String, callback: () -> Unit) {
    setGenericSpan(
        object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }

            override fun onClick(view: View) {
                callback()
            }
        },
        phrase
    )
}

private fun SpannableStringBuilder.setGenericSpan(what: Any, phrase: String) {
    setSpan(what, length - phrase.length, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}