package com.jasonsb.simplebible

interface BaseParser {
    fun parsePassages(passages: List<String>): CharSequence
}