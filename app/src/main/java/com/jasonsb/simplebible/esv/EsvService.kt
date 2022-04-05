package com.jasonsb.simplebible.esv

import com.jasonsb.simplebible.Instance
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface EsvService {
    companion object {
        private const val API_URL = "https://api.esv.org/v3/"

        fun create(): EsvService {
            val retrofit = Retrofit.Builder()
                .client(Instance.okHttpClient)
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(EsvService::class.java)
        }
    }

    /**
     * @param passage This is the requested passage. We try our best to parse a meaningful
     * passage reference from this value. Here are some examples of what's accepted:
     * - John 1:1
     * - jn11.35
     * - Genesis 1-3
     * - John1.1;Genesis1.1
     * @param includePassageReferences Include the passage reference before the text.
     * @param includeVerseNumbers Include verse numbers.
     * @param includeFirstVerseNumbers Include the verse number for the first verse of a chapter.
     * @param includeFootnotes Include callouts to footnotes in the text.
     * @param includeFootnoteBody Include footnote bodies below the text. Only works if
     * [includeFootnotes] is also `true`.
     * @param includeHeadings Include section headings. For example, the section heading of Matthew
     * 5 is "The Sermon on the Mount".
     * @param includeShortCopyright Include "(ESV)" at the end of the text. Mutually exclusive with
     * [includeCopyright]. This fulfills your copyright display requirements.
     * @param includeCopyright Include a copyright notice at the end of the text. Mutually exclusive
     * with [includeShortCopyright]. This fulfills your copyright display requirements.
     */
    @GET("passage/text")
    fun getPassage(
        @Query("q")
        passage: String,
        @Query("include-passage-references")
        includePassageReferences: Boolean = true,
        @Query("include-verse-numbers")
        includeVerseNumbers: Boolean = true,
        @Query("include-first-verse-numbers")
        includeFirstVerseNumbers: Boolean = true,
        @Query("include-footnotes")
        includeFootnotes: Boolean = true,
        @Query("include-footnote-body")
        includeFootnoteBody: Boolean = true,
        @Query("include-headings")
        includeHeadings: Boolean = true,
        @Query("include-short-copyright")
        includeShortCopyright: Boolean = true,
        @Query("include-copyright")
        includeCopyright: Boolean = false,
        @Query("include-passage-horizontal-lines")
        includePassageHorizontalLines: Boolean = false
    ): Call<EsvPassageResponse>
}