package io.kenji.sample.mastodonclient

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kenji.sample.mastodonclient.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainFragment : Fragment(R.layout.fragment_main) {

    companion object {
        // ログ出力用のタグ
        private val TAG = MainFragment::class.java.simpleName
        // アクセスするMastodonインスタンスのURL
        private const val API_BASE_URL = "https://androidbook2020.keiji.io"
    }

    // moshiのインスタンスを作成
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // RetrofitでAPIにアクセスする準備．アクセス先のURLとAPIの定義をして初期化する
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        // Moshiを使ってJsonをパースするようにRetrofitに登録（追加）
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    private val api = retrofit.create(MastodonApi::class.java)

    private  var binding: FragmentMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)
        binding?.button?.setOnClickListener {
            binding?.button?.text = "clicked"
            // IOのメインスレッドで実行
            CoroutineScope(Dispatchers.IO).launch{
                val toolList = api.fetchPublicTimeline()
                // withContextで処理を実行するスレッドを指定
                showTootList(toolList)
            }
        }
    }

    // Tootオブジェクトのリストについて各要素のdisplayNameをボタンに表示
    private suspend fun showTootList(
        tootList: List<Toot>
    ) = withContext(Dispatchers.Main){
        // ?:　はエルビス演算子，bindingがnullの場合，return@withContextでメソッドを終了
        // withContextの中にあるため，returnではなく，return@withContextを明示する必要がある
        val binding = binding ?: return@withContext
        // TootのアカウントのdisplayNameだけを取り出してリストに変換
        val accountNameList = tootList.map { it.account.displayName }
        // 最終的にjoinToStringでそれぞれの要素を改行で繋いで，ボタンのラベルに設定
        binding.button.text = accountNameList.joinToString("\n")
    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//
//        binding?.unbind()
//    }
}