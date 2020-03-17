package eu.vmladenov.amymoney.ui.views.transactions

import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.Transaction

data class TransactionsFilter (
    val counterAccount: Account,
    val categoryId: String = "",
    val payeeId: String = "",
    val tagId: String = "",
    val memo: String = ""
) {
    fun checkTransaction(transaction: Transaction): Boolean {
        return transaction.splits.any { s -> s.accountId == counterAccount.id } ||
               categoryId.isNotEmpty() && transaction.splits.any { s -> s.accountId == categoryId } ||
               payeeId.isNotEmpty() && transaction.splits.any { s -> s.payeeId == payeeId } ||
               tagId.isNotEmpty() && transaction.splits.any { s -> s.tagIds.any { t -> t == tagId } } ||
               memo.isNotEmpty() && transaction.splits.any { s -> s.memo.contains(memo, true) }
    }
}
