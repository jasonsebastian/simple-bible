package com.jasonsb.simplebible.exts

import com.jasonsb.simplebible.esv.Footnote


fun List<Footnote>.getExplanationByFootnoteIndex(footnoteIndex: String) =
    find { it.index == footnoteIndex }?.explanation