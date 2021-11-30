package io.kenji.sample.mastodonclient

import retrofit2.http.GET

interface MastodonApi {

    // 公開（public）タイムラインにアクセスするAPIを宣言
    @GET("api/v1/timelines/public")
    // suspendキーワードをつけて中断関数として定義
    suspend fun fetchPublicTimeline(
    ):List<Toot> // Callではなく，直接List<Toot>を返す
}