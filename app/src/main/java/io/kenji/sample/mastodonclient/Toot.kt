package io.kenji.sample.mastodonclient

import com.squareup.moshi.Json

data class Toot (
    val id: String,
    @Json(name = "created_at") val createdAt: String,
    val url: String,
    val content: String,
    // accountキーの示す連想配列をAccountクラスのオブジェクトに変換する
    val account: Account
    )