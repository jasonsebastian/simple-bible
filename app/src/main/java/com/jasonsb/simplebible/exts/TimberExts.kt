package com.jasonsb.simplebible.exts

import timber.log.Timber


fun debugList(list: List<Any>, heading: String) {
    Timber.d(heading)
    Timber.d(list.joinToString("\n") { "'$it'" })
}

fun debugString(message: String, heading: String) {
    Timber.d(heading)
    Timber.d(message)
}