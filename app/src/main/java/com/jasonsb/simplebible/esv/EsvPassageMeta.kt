package com.jasonsb.simplebible.esv

import com.google.gson.annotations.SerializedName

data class EsvPassageMeta(
    @SerializedName("canonical")
    val canonical: String,
    @SerializedName("chapter_end")
    val chapterEnd: List<Int>,
    @SerializedName("chapter_start")
    val chapterStart: List<Int>,
    @SerializedName("next_chapter")
    val nextChapter: List<Int>,
    @SerializedName("next_verse")
    val nextVerse: Int,
    @SerializedName("prev_chapter")
    val prevChapter: List<Int>,
    @SerializedName("prev_verse")
    val prevVerse: Int
)