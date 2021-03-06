package eu.vmladenov.amymoney.ui.views.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.AccountComparable
import kotlinx.android.synthetic.main.accounts_list_item.view.*


class AccountsAdapter(val clickHandler: AccountClickHandler) : ListAdapter<Account, AccountsAdapter.ViewHolder>(AccountComparable) {

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Account
        clickHandler.handle(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.accounts_list_item, parent, false) as LinearLayout
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val itemLayout: LinearLayout) : RecyclerView.ViewHolder(itemLayout) {
        private val nameView: TextView = itemLayout.account_item_name
        private val typeView: TextView = itemLayout.account_item_type

        fun bind(account: Account) {
            with(itemLayout) {
                tag = account
                setOnClickListener(onClickListener)
            }
            nameView.text = account.name
            typeView.text = account.type.name
        }
    }
}
