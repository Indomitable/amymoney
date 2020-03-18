package eu.vmladenov.amymoney.ui.views.payees

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.models.Payee

class PayeesAdapter(private val clickHandler: PayeeClickHandler): RecyclerView.Adapter<PayeesAdapter.PayeesViewHolder>() {

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Payee
        clickHandler.handle(item)
    }

    private val items = mutableListOf<Payee>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayeesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView

        return PayeesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PayeesViewHolder, position: Int) {
        val payee = items[position]
        holder.bind(payee)
    }

    fun updateItems(payees: List<Payee>) {
        items.clear()
        items.addAll(payees)
        notifyDataSetChanged()
    }

    inner class PayeesViewHolder(private val textView: TextView): RecyclerView.ViewHolder(textView) {
        fun bind(payee: Payee) {
            with(textView) {
                tag = payee
                text = payee.name
                setOnClickListener(onClickListener)
            }
        }
    }
}

interface PayeeClickHandler {
    fun handle(payee: Payee)
}
