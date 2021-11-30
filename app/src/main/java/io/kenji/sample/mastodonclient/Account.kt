package io.kenji.sample.mastodonclient

import com.squareup.moshi.Json

// データクラスとして宣言
data class Account (
    val id: String,
    val username: String,
    // kotlinの変数名と異なるキーを指定
    @Json(name = "display_name") val displayName: String,
    val url: String
)