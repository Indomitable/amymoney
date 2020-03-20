package eu.vmladenov.amymoney.ui.views.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.ui.views.accounts.AccountClickHandler
import kotlinx.android.synthetic.main.account_balance_list_item.view.*
import java.text.NumberFormat
import java.util.*

class AccountBalanceComparable: DiffUtil.ItemCallback<AccountBalance>() {
    override fun areItemsTheSame(oldItem: AccountBalance, newItem: AccountBalance): Boolean {
        return oldItem.account.id == newItem.account.id
    }

    override fun areContentsTheSame(oldItem: AccountBalance, newItem: AccountBalance): Boolean {
        return oldItem == newItem
    }
}

class AccountBalanceAdapter: ListAdapter<AccountBalance, AccountBalanceAdapter.ViewHolder>(AccountBalanceComparable()) {
    var clickHandler: AccountClickHandler? = null


    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as AccountBalance
        clickHandler?.handle(item.account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountBalanceAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_balance_list_item, parent, false) as LinearLayout
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AccountBalanceAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val itemLayout: LinearLayout) : RecyclerView.ViewHolder(itemLayout) {
        private val accountView: TextView = itemLayout.account_balance_item_name
        private val balanceView: TextView = itemLayout.account_balance_item_value

        fun bind(accountBalance: AccountBalance) {
            with(itemLayout) {
                tag = accountBalance
                setOnClickListener(onClickListener)
            }
            accountView.text = accountBalance.account.name
            val format: NumberFormat = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance(accountBalance.account.currencyId)
            balanceView.text = format.format(accountBalance.balance.toDecimal().toDouble())
        }
    }
}
