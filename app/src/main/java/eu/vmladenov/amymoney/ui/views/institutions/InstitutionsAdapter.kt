package eu.vmladenov.amymoney.ui.views.institutions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.models.InstitutionComparable

class InstitutionsAdapter(private val clickHandler: InstitutionClickHandler) :
    ListAdapter<Institution, InstitutionsAdapter.ViewHolder>(
        InstitutionComparable
    ) {
    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Institution
        clickHandler.handle(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(institution: Institution) {
            with(textView) {
                tag = institution
                text = institution.name
                setOnClickListener(onClickListener)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + textView.text + "'"
        }
    }
}
