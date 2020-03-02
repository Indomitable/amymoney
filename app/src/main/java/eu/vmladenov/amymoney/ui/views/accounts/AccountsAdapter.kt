package eu.vmladenov.amymoney.ui.views.accounts

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.AccountComparable


class AccountsAdapter(
    private val clickHandler: AccountClickHandler
) : ListAdapter<Account, AccountsAdapter.ViewHolder>(AccountComparable) {

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Account
        clickHandler.handle(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView) {
        fun bind(account: Account) {
            with(textView) {
                tag = account
                text = account.name
                setOnClickListener(onClickListener)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + textView.text + "'"
        }
    }
}
