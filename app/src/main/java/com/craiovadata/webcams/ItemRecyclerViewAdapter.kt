package com.craiovadata.webcams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.craiovadata.webcams.UiUtil.Companion.loadWebcamImage
import com.craiovadata.webcams.model.Content

class ItemRecyclerViewAdapter(
    private val parentActivity: ItemListActivity,
    private val twoPane: Boolean
) :
    RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    var values: List<Content.Item> = listOf()
//    private val onClickListener: View.OnClickListener

    init {
//        onClickListener = View.OnClickListener { v ->
//            val item = v.tag as Content.Item
//            if (twoPane) {
//                val fragment = ItemDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                    }
//                }
//                parentActivity.supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.item_detail_container, fragment)
//                    .commit()
//            } else {
//                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
//                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                }
//                v.context.startActivity(intent)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textView.text = item.text
        holder.imageView.loadWebcamImage(item.url)

        with(holder.itemView) {
            tag = item
//            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textViewImageDescription)
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }
}