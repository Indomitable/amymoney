package eu.vmladenov.amymoney.ui.views.transactions

import eu.vmladenov.amymoney.models.Transaction

data class TransactionsFilter (
    val counterAccountId: String,
    val categoryId: String = "",
    val payeeId: String = "",
    val tagId: String = "",
    val memo: String = ""
) {
    fun checkTransaction(transaction: Transaction): Boolean {
        return counterAccountId.isNotEmpty() && transaction.splits.any { s -> s.accountId == counterAccountId } ||
               categoryId.isNotEmpty() && transaction.splits.any { s -> s.accountId == counterAccountId } ||
               payeeId.isNotEmpty() && transaction.splits.any { s -> s.payeeId == payeeId } ||
               tagId.isNotEmpty() && transaction.splits.any { s -> s.tagIds.any { t -> t == tagId } } ||
               memo.isNotEmpty() && transaction.splits.any { s -> s.memo.contains(memo, true) }
    }
}
