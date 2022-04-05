package com.jasonsb.simplebible

import com.jasonsb.simplebible.esv.SpecialChar

class RegexUtil {
    companion object {
        const val OR = "|"
        private const val VERSE_NUMBER_MATCH = """\[(\d+)]"""
        const val FOOTNOTE_INDEX_MATCH = """\((\d+)\)"""
        private const val KEYWORD_MATCH = """\*([^*]+)\*"""
        private const val HEADING_MATCH = """\n\s*\n\s*([^\[]+)\n\n"""
        const val FOOTNOTE_SEPARATOR = """\n+"""
        const val FOOTNOTE_EXPLANATION_MATCH = """([A-Z].+)"""

        val verseNumberSpecialChar =
            SpecialChar(first = "[", last = "]", match = VERSE_NUMBER_MATCH)

        val footnoteIndexSpecialChar =
            SpecialChar(first = "(", last = ")", match = FOOTNOTE_INDEX_MATCH)

        val keywordSpecialChar =
            SpecialChar(first = "*", last = "*", match = KEYWORD_MATCH)

        val headingSpecialChar =
            SpecialChar(first = "\n\n", last = "\n\n", match = HEADING_MATCH)

        val matches: List<SpecialChar> = listOf(
            verseNumberSpecialChar,
            footnoteIndexSpecialChar,
            keywordSpecialChar,
            headingSpecialChar
        )

        fun findOccurrence(match: String, input: String): String? {
            return match.toRegex().find(input)?.destructured?.toList()?.first()
        }
    }
}