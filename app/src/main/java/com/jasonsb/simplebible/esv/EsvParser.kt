package com.jasonsb.simplebible.esv

import android.content.Context
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.jasonsb.simplebible.BaseParser
import com.jasonsb.simplebible.R
import com.jasonsb.simplebible.RegexUtil
import com.jasonsb.simplebible.RegexUtil.Companion.FOOTNOTE_SEPARATOR
import com.jasonsb.simplebible.RegexUtil.Companion.findOccurrence
import com.jasonsb.simplebible.RegexUtil.Companion.footnoteIndexSpecialChar
import com.jasonsb.simplebible.RegexUtil.Companion.headingSpecialChar
import com.jasonsb.simplebible.RegexUtil.Companion.keywordSpecialChar
import com.jasonsb.simplebible.RegexUtil.Companion.verseNumberSpecialChar
import com.jasonsb.simplebible.exts.*

class EsvParser(private val context: Context) : BaseParser {

    companion object {
        const val FOOTNOTES = "Footnotes"
    }

    private val debugMode: Boolean = true

    override fun parsePassages(passages: List<String>): CharSequence {
        return passages.map {
            if (it.isEmpty()) {
                return SpannableStringBuilder("")
            }
            val footnotes = getFootnotesFromPassage(it)
            if (debugMode) {
                debugList(footnotes, heading = "Footnotes:")
            }
            getRichText(text = removeFootnotes(it), footnotes = footnotes)
        }.reduce { sum, element -> sum.append(element) }.trimEnd()
    }

    private fun getFootnotesFromPassage(passage: String): List<Footnote> {
        if (FOOTNOTES !in passage) {
            return emptyList()
        }
        val footnoteText: List<String> = passage.substringAfter(FOOTNOTES)
            .trim()
            .split(FOOTNOTE_SEPARATOR.toRegex())
            .map { it.trim() }
        return footnoteText.map {
            Footnote(
                index = findOccurrence(
                    match = RegexUtil.FOOTNOTE_INDEX_MATCH,
                    input = it
                )!!,
                explanation = findOccurrence(
                    match = RegexUtil.FOOTNOTE_EXPLANATION_MATCH,
                    input = it
                )!!
            )
        }
    }

    private fun removeFootnotes(result: String): String {
        return if (FOOTNOTES in result) {
            result.substringBefore(FOOTNOTES)
        } else {
            result
        }
    }

    private fun getRichText(
        text: String,
        footnotes: List<Footnote>? = null
    ): SpannableStringBuilder {
        val tokens = text.getTokens()
        return SpannableStringBuilder().apply {
            for (token in tokens) {
                var tokenMatched = false
                for (match in RegexUtil.matches.map { it.match }) {
                    val phrase = findOccurrence(match = match, input = token)
                    if (phrase != null) {
                        tokenMatched = true
                        when (match) {
                            verseNumberSpecialChar.match -> {
                                append(phrase)
                                setVerseNumberSpan(phrase = phrase)
                            }
                            footnoteIndexSpecialChar.match -> {
                                append(phrase)
                                setFootnoteIndexSpan(
                                    phrase = phrase,
                                    footnoteText = footnotes?.getExplanationByFootnoteIndex(phrase)
                                        ?: context.getString(R.string.no_footnote_entry_found)
                                )
                            }
                            keywordSpecialChar.match -> {
                                append(phrase)
                                setKeywordSpan(phrase = phrase)
                            }
                            headingSpecialChar.match -> {
                                append(headingSpecialChar.first)
                                append(phrase)
                                setHeadingSpan(phrase = phrase)
                                append(headingSpecialChar.last)
                            }
                        }
                        break
                    }
                }
                if (!tokenMatched) {
                    append(token)
                }
            }
        }
    }

    private fun String.getTokens(): List<String> {
        val ranges = getRanges()
        if (ranges.isEmpty()) {
            return listOf(this)
        }
        val phraseTokens = getPhraseTokens(ranges)
        if (debugMode) {
            debugList(phraseTokens, heading = "Tokens:")
        }
        return phraseTokens
    }

    private fun String.getPhraseTokens(ranges: List<IntRange>): List<String> {
        val tokens = mutableListOf<PhraseToken>()
        if (ranges.first().first > 0) {
            tokens.add(PhraseToken(0, ranges.first().first))
        }
        for (i in ranges.indices) {
            tokens.add(PhraseToken(start = ranges[i].first, end = ranges[i].last + 1))
            val start = ranges[i].last + 1
            val end = if ((i + 1) in ranges.indices) {
                ranges[i + 1].first
            } else {
                length
            }
            if (start != end) {
                tokens.add(PhraseToken(start = start, end = end))
            }
        }
        return tokens.map { substring(it.start, it.end) }
    }

    private fun String.getRanges(): List<IntRange> {
        val allRegex = RegexUtil.matches.joinToString(RegexUtil.OR) { it.match }.toRegex()
        val ranges: List<IntRange> = allRegex.findAll(this).map { it.range }.toList()
        if (debugMode) {
            debugList(ranges.map { substring(it) }, heading = "Ranges:")
        }
        return ranges
    }

    private fun SpannableStringBuilder.setVerseNumberSpan(phrase: String) {
        setSuperscriptSpan(
            phrase,
            color = ContextCompat.getColor(context, R.color.black),
            isBold = true
        )
    }

    private fun SpannableStringBuilder.setFootnoteIndexSpan(phrase: String, footnoteText: String) {
        setSuperscriptSpan(
            phrase,
            color = ContextCompat.getColor(context, R.color.purple_500)
        ) {
            if (debugMode) {
                debugString("'$footnoteText'", heading = "Footnote:")
            }
            Toast.makeText(context, getRichText(text = footnoteText), Toast.LENGTH_LONG).show()
        }
    }

    private fun SpannableStringBuilder.setKeywordSpan(phrase: String) {
        setItalicSpan(phrase)
    }

    private fun SpannableStringBuilder.setHeadingSpan(phrase: String) {
        setBoldSpan(phrase)
    }
}

data class PhraseToken(val start: Int, val end: Int)

data class SpecialChar(val first: String, val last: String, val match: String)

data class Footnote(val index: String, val explanation: String)