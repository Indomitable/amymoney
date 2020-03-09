package eu.vmladenov.amymoney.ui.views.accounts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import eu.vmladenov.amymoney.models.Institution

class InstitutionsSpinnerAdapter : BaseAdapter() {
    private val items: MutableList<Institution> = mutableListOf()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context)
                            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false) as TextView

        view.text = items[position].name
        return view

    }

    override fun getItem(position: Int): Institution {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    fun getPosition(institution: Institution?): Int {
        if (institution != null) {
            return items.indexOfFirst { i -> i.id == institution.id }
        }
        return -1
    }

    fun fill(institutions: Sequence<Institution>) {
        items.clear()
        items.addAll(institutions)
        notifyDataSetChanged()
    }
}
