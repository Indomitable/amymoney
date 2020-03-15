package eu.vmladenov.amymoney.ui.views.transactions

import eu.vmladenov.amymoney.models.Transaction

data class TransactionsFilter (
    val accountId: String,
    val categoryId: String = "",
    val payeeId: String = "",
    val tagId: String = "",
    val memo: String = ""
) {
    fun checkTransaction(transaction: Transaction): Boolean {
        return accountId.isNotEmpty() && transaction.splits.any { s -> s.accountId == accountId } ||
               categoryId.isNotEmpty() && transaction.splits.any { s -> s.accountId == accountId } ||
               payeeId.isNotEmpty() && transaction.splits.any { s -> s.payeeId == payeeId } ||
               tagId.isNotEmpty() && transaction.splits.any { s -> s.tagIds.any { t -> t == tagId } } ||
               memo.isNotEmpty() && transaction.splits.any { s -> s.memo.contains(memo, true) }
    }
}
