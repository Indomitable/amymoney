package eu.vmladenov.amymoney.ui.views.tags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.models.Tag

class TagsAdapter(private val clickHandler: TagClickHandler): RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Tag
        clickHandler.handle(item)
    }

    private val items = mutableListOf<Tag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView

        return TagsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        val tag = items[position]
        holder.bind(tag)
    }

    fun updateItems(tags: List<Tag>) {
        items.clear()
        items.addAll(tags)
        notifyDataSetChanged()
    }

    inner class TagsViewHolder(private val textView: TextView): RecyclerView.ViewHolder(textView) {
        fun bind(tagModel: Tag) {
            with(textView) {
                tag = tagModel
                text = tagModel.name
                setOnClickListener(onClickListener)
            }
        }
    }
}

interface TagClickHandler {
    fun handle(tag: Tag)
}
