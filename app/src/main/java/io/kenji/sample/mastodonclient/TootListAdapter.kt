package io.kenji.sample.mastodonclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.kenji.sample.mastodonclient.databinding.ListItemTootBinding

class TootListAdapter (
    private val layoutInflater: LayoutInflater,
    private val tootList: ArrayList<Toot>
    ) : RecyclerView.Adapter<TootListAdapter.ViewHolder>() { // RecyclerView.Adapterを継承する

    // リストの要素数を知らせる
    override fun getItemCount() = tootList.size

    // viewタイプに応じたViewHolderのインスタンスを生成する．
    // 今回はViewHolderは1つだけviewTypeは考慮しない
    override fun onCreateViewHolder(
        parent : ViewGroup,
        viewType : Int
    ) : ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemTootBinding>(
            layoutInflater,
            R.layout.list_item_toot,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    // onCreateViewHolderで生成したViewHolderインスタンスに，リストのpositionで示される位置の要素をバインドする
    override fun onBindViewHolder(
        holder : ViewHolder,
        position : Int
    ) {
        holder.bind(tootList[position])
    }

    class ViewHolder( // Tootオブジェクトの内容をDataBindingに表示する
        private val binding : ListItemTootBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toot: Toot) {
            binding.toot = toot
        }
    }
}