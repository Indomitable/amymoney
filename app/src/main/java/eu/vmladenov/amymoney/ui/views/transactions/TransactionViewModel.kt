package eu.vmladenov.amymoney.ui.views.transactions

import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Split
import eu.vmladenov.amymoney.models.Transaction
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

interface ITransactionViewModelFactory {
    fun map(transaction: Transaction, counterAccountId: String): TransactionViewModel
}

class TransactionViewModelFactory @Inject constructor(private val repository: IAMyMoneyRepository) : ITransactionViewModelFactory {
    companion object {
        const val UNASSIGNED = "*** UNASSIGNED ***"
        const val SPLIT_TRANSACTIONS = "Split transaction"
    }

    override fun map(transaction: Transaction, counterAccountId: String): TransactionViewModel {
        val splits = getSplits(transaction, counterAccountId)
        return TransactionViewModel(
            id = transaction.id,
            splitId = splits.first.id,
            payee = getPayeeName(transaction, splits),
            date = transaction.postDate,
            value = getValue(splits),
            currency = transaction.commodity,
            number = splits.first.number.toIntOrNull()
        )
    }

    private fun getSplits(transaction: Transaction, counterAccountId: String): Pair<Split, List<Split>> {
        var counterAccountSplit: Split? = null
        val otherAccountsSplits = mutableListOf<Split>()
        for (split in transaction.splits) {
            if (split.accountId == counterAccountId) {
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

    private fun getValue(splits: Pair<Split, List<Split>>): BigDecimal {
        return splits.first.value.toDecimal()
    }
}

data class TransactionViewModel(
    val id: String,
    val splitId: String,
    val payee: String,
    val date: Date?,
    val value: BigDecimal,
    val currency: String,
    val number: Int?
)
