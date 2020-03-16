package eu.vmladenov.amymoney.ui.views.transactions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Split
import eu.vmladenov.amymoney.models.Transaction
import eu.vmladenov.amymoney.models.TransactionComparable
import kotlinx.android.synthetic.main.transaction_list_item.view.*
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

class TransactionsAdapter(private val repository: IAMyMoneyRepository) :
    ListAdapter<Transaction, TransactionsAdapter.ViewHolder>(TransactionComparable) {

    private var counterAccount: String = ""
    var clickHandler: TransactionClickHandler? = null
    private val UNASSIGNED = "*** UNASSIGNED ***"
    private val SPLIT_TRANSACTIONS = "Split transaction"


    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        val item = v.tag as Transaction
        clickHandler?.handle(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_list_item, parent, false) as LinearLayout
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun setSource(counterAccount: String, transactions: List<Transaction>) {
        this.counterAccount = counterAccount
        submitList(transactions.sortedByDescending { t -> t.postDate })
    }

    inner class ViewHolder(val itemLayout: LinearLayout) : RecyclerView.ViewHolder(itemLayout) {
        private val payeeView: TextView = itemLayout.transaction_item_payee
        private val dateView: TextView = itemLayout.transaction_item_date
        private val valueView: TextView = itemLayout.transaction_item_value

        fun bind(transaction: Transaction) {
            with(itemLayout) {
                tag = transaction
                setOnClickListener(onClickListener)
            }
            if (transaction.postDate != null) {
                dateView.text = DateFormat.getDateInstance(DateFormat.SHORT).format(transaction.postDate)
            }
            val splits = getSplits(transaction)
            payeeView.text = getPayeeName(transaction, splits)
            valueView.text = getValue(transaction, splits)
        }

        private fun getSplits(transaction: Transaction): Pair<Split, List<Split>> {
            var counterAccountSplit: Split? = null
            val otherAccountsSplits = mutableListOf<Split>()
            for (split in transaction.splits) {
                if (split.accountId == counterAccount) {
                    // the counter account for the transaction always is the user account
                    counterAccountSplit = split
                } else {
                    otherAccountsSplits.add(split)
                }
            }
            return Pair(counterAccountSplit!!, otherAccountsSplits)
        }

        private fun getPayeeName(transaction: Transaction, splits: Pair<Split, List<Split>>): String {
            val payee = if (splits.first.payeeId.isNotEmpty()) repository.currentPayees[splits.first.payeeId]?.name else null
            if (!payee.isNullOrEmpty())
                // if payee exists return its name
                return payee
            if (transaction.memo.isNotEmpty()) {
                // if has memo return memo
                return transaction.memo
            }
            // check other splits
            return when (splits.second.size) {
                0 -> UNASSIGNED // when no other splits not assigned.
                1 -> {
                    val otherAccount = repository.currentAccounts[splits.second[0].accountId]
                    otherAccount?.name ?: UNASSIGNED // Exactly one other split get category from it, if no category return unassigned
                }
                else -> SPLIT_TRANSACTIONS // if more than 1 other split return split transaction
            }
        }

        private fun getValue(transaction: Transaction, splits: Pair<Split, List<Split>>): String {
            val format: NumberFormat = NumberFormat.getCurrencyInstance()
            format.currency = Currency.getInstance(transaction.commodity)
            return format.format(splits.first.value.toDecimal().toDouble())
        }
    }
}
