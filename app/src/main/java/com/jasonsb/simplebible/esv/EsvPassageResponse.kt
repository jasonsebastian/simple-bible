package com.jasonsb.simplebible.esv

import com.jasonsb.simplebible.network.BaseResponse
import com.google.gson.annotations.SerializedName

data class EsvPassageResponse(
    @SerializedName("canonical")
    val canonical: String,
    @SerializedName("parsed")
    val parsed: List<List<Int>>,
    @SerializedName("passage_meta")
    val passageMeta: List<EsvPassageMeta>,
    @SerializedName("passages")
    val passages: List<String>,
    @SerializedName("query")
    val query: String
) : BaseResponse