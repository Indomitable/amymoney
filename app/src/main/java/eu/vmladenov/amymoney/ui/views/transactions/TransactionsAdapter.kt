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
import java.util.*
import kotlin.math.min


interface ItemViewModel

data class TransactionItemViewModel(val transaction: TransactionViewModel) : ItemViewModel
data class DescriptionItemViewModel(val text: String) : ItemViewModel

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.BaseViewHolder>() {

    private lateinit var counterAccount: Account
    private lateinit var transactions: List<TransactionViewModel>
    private lateinit var formatter: NumberFormat
    private val dateFormatter = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())

    private var clickHandler: TransactionClickHandler? = null
    private var loadedTransactionsCount: Int = 0

    // this is the list of only current shown transactions, we going to add more as we scroll
    private var currentViewTransactions = mutableListOf<ItemViewModel>()

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as TransactionViewModel
        clickHandler?.handle(item.id)
    }

    fun update(
        counterAccount: Account,
        transactions: List<TransactionViewModel>,
        formatter: NumberFormat
    ) {
        this.counterAccount = counterAccount
        this.transactions = transactions
        this.formatter = formatter
        this.loadedTransactionsCount = 0
        appendDescription(DescriptionItemViewModel("Transactions..."))
        loadMoreTransactions()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == 0) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_list_item, parent, false) as LinearLayout
            return TransactionViewHolder(itemView)
        }
        if (viewType == 1) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.transaction_list_description_item, parent, false) as TextView
            return DescriptionViewHolder(itemView)
        }
        throw Exception("Not supported view type")
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = currentViewTransactions[position]
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 1
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        return currentViewTransactions.size
    }

    fun loadMoreTransactions() {
        val from = loadedTransactionsCount
        val maxSize = transactions.size
        if (from == maxSize) {
            return
        }
        val toIndex = min(maxSize, from + 20)

        val newTransactions = transactions.subList(from, toIndex)
            .map { TransactionItemViewModel(it) }
        appendTransactions(newTransactions)
    }

    private fun appendTransactions(items: List<TransactionItemViewModel>) {
        val positionStart = currentViewTransactions.size
        currentViewTransactions.addAll(items)
        loadedTransactionsCount += items.size
        notifyItemRangeInserted(positionStart, items.size)
    }

    private fun appendDescription(descr: DescriptionItemViewModel) {
        currentViewTransactions.add(descr)
        notifyItemInserted(currentViewTransactions.size - 1)
    }

    abstract inner class BaseViewHolder(container: View): RecyclerView.ViewHolder(container) {
        abstract fun bind(itemViewModel: ItemViewModel)
    }

    inner class TransactionViewHolder(private val itemLayout: LinearLayout) : BaseViewHolder(itemLayout) {
        private val payeeView: TextView = itemLayout.transaction_item_payee
        private val dateView: TextView = itemLayout.transaction_item_date
        private val valueView: TextView = itemLayout.transaction_item_value

        override fun bind(itemViewModel: ItemViewModel) {
            val transaction = (itemViewModel as TransactionItemViewModel).transaction
            itemLayout.tag = itemViewModel
            itemLayout.setOnClickListener(onClickListener)
            if (transaction.date != null) {
                dateView.text = dateFormatter.format(transaction.date)
            }
            payeeView.text = transaction.payee
            valueView.text = formatter.format(transaction.value)
        }
    }

    inner class DescriptionViewHolder(private val textView: TextView) : BaseViewHolder(textView) {
        override fun bind(itemViewModel: ItemViewModel) {
            val description = (itemViewModel as DescriptionItemViewModel).text
            textView.text = description
        }
    }
}
