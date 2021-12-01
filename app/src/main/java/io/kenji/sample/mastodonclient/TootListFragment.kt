package io.kenji.sample.mastodonclient

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.kenji.sample.mastodonclient.databinding.FragmentTootListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TootListFragment : Fragment(R.layout.fragment_toot_list) {

    companion object {
        val TAG = TootListFragment::class.java.simpleName

        private const val API_BASE_URL = "https://androidbook2020.kenji.io"
    }

    private var binding: FragmentTootListBinding? = null

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    private val api = retrofit.create(MastodonApi::class.java)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private lateinit var adapter: TootListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    // 読み込み済みのTootのリストをクラスのメンバ変数で保持
    private val tootListA = ArrayList<Toot>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TootListAdapterをインスタンス化する．コンストラクタにtootListを与える
        adapter = TootListAdapter(layoutInflater, tootListA)
        // 表示するリストの並べ方を指定
        // VERTICALは縦方向を指定
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
        val bindingData: FragmentTootListBinding? = DataBindingUtil.bind(view)

        binding = bindingData ?: return

        // TootListAdapter（表示内容）とLayoutManager（レイアウト方法）をRecyclerViewに設定
        bindingData.recyclerView.also {
            it.layoutManager = layoutManager
            it.adapter = adapter
        }

        coroutineScope.launch {
            val tootList = api.fetchPublicTimeline()
            // APIから取得したTootのリストをメンバ変数のリストに追加して表示内容を再度読み込み
            tootListA.addAll(tootList)
            reloadTootList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.unbind()
    }

    private suspend fun reloadTootList() = withContext(Dispatchers.Main) {
        // Adapterにデータが更新されたことを伝える
        adapter.notifyDataSetChanged()
    }
}
