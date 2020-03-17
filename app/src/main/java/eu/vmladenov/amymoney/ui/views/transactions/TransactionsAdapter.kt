package eu.vmladenov.amymoney.ui.views.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Account
import kotlinx.android.synthetic.main.transaction_list_item.view.*
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Currency
import kotlin.math.min


class TransactionsAdapter: RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    private lateinit var counterAccount: Account
    private lateinit var transactions: List<TransactionViewModel>
    private lateinit var format: NumberFormat

    private var clickHandler: TransactionClickHandler? = null

    // this is the list of only current shown transactions, we going to add more as we scroll
    private var currentViewTransactions = mutableListOf<TransactionViewModel>()

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as TransactionViewModel
        clickHandler?.handle(item.id)
    }

    fun update(counterAccount: Account, transactions: List<TransactionViewModel>) {
        this.counterAccount = counterAccount
        this.transactions = transactions
        this.format = NumberFormat.getCurrencyInstance().also {
            it.currency = Currency.getInstance(counterAccount.currencyId)
        }
        loadMoreTransactions()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list_item, parent, false) as LinearLayout
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentViewTransactions[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return currentViewTransactions.size
    }

    fun loadMoreTransactions() {
        val from = currentViewTransactions.size
        val maxSize = transactions.size
        if (from == maxSize) {
            return
        }
        val toIndex = min(maxSize, from + 20)

        val newTransactions = transactions.subList(from, toIndex)
        for (transaction in newTransactions) {
            currentViewTransactions.add(transaction)
            notifyItemInserted(currentViewTransactions.size - 1)
        }
    }

    inner class ViewHolder(private val itemLayout: LinearLayout) : RecyclerView.ViewHolder(itemLayout) {
        private val payeeView: TextView = itemLayout.transaction_item_payee
        private val dateView: TextView = itemLayout.transaction_item_date
        private val valueView: TextView = itemLayout.transaction_item_value


        fun bind(transaction: TransactionViewModel) {
            with(itemLayout) {
                tag = transaction
                setOnClickListener(onClickListener)
            }
            if (transaction.date != null) {
                dateView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(transaction.date)
            }
            payeeView.text = transaction.payee
            valueView.text = format.format(transaction.value)
        }
    }
}
