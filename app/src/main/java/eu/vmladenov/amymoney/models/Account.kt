package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.util.*
import kotlin.Exception

enum class AccountStandardType(val id: String) {
    Liability("AStd::Liability"),
    Asset("AStd::Asset"),
    Expense("AStd::Expense"),
    Income("AStd::Income"),
    Equity("AStd::Equity");
}

enum class AccountType(val type: Int) {
    Unknown(0),          /**< For error handling */
    Checkings(1),            /**< Standard checking account */
    Savings(2),              /**< Typical savings account */
    Cash(3),                 /**< Denotes a shoe-box or pillowcase stuffed with cash */
    CreditCard(4),           /**< Credit card accounts */
    Loan(5),                 /**< Loan and mortgage accounts (liability) */
    CertificateDep(6),       /**< Certificates of Deposit */
    Investment(7),           /**< Investment account */
    MoneyMarket(8),          /**< Money Market Account */
    Asset(9),                /**< Denotes a generic asset account.*/
    Liability(10),            /**< Denotes a generic liability account.*/
    Currency(11),             /**< Denotes a currency trading account. */
    Income(12),               /**< Denotes an income account */
    Expense(13),              /**< Denotes an expense account */
    AssetLoan(14),            /**< Denotes a loan (asset of the owner of this object) */
    Stock(15),                /**< Denotes an security account as sub-account for an investment */
    Equity(16);               /**< Denotes an equity account e.g. opening/closing balance */

    companion object {
        private val map = values().associateBy(AccountType::type)

        operator fun get(type: Int): AccountType {
            return map.getOrElse(type) { Unknown }
        }
    }
}


@XmlTag(XmlTags.Account)
data class Account(
    @XmlAttribute("id") override val id: String,
    @XmlAttribute("name") val name: String,
    @XmlAttribute("currency") val currencyId: String,
    @XmlAttribute("type") val type: AccountType,
    @XmlAttribute("lastreconciled") val lastReconciliationDate: Date?,
    @XmlAttribute("lastmodified") val lastModified: Date?,
    @XmlAttribute("parentaccount") val parentAccountId: String,
    @XmlAttribute("opened") val openingDate: Date?,
    @XmlAttribute("institution") val institutionId: String,
    @XmlAttribute("number") val number: String,
    @XmlAttribute("description") val description: String,
    // @XmlAttribute("openingbalance") val openingBalance: String,
    // @XmlAttribute("iban") val iban: String,
    // @XmlAttribute("bic") val bic: String
    val subAccounts: List<String>,
    val extra: Map<String, String>
): IModel

@XmlTag(XmlTags.Accounts)
@XmlCollection(Account::class)
class Accounts() : BaseMap<Account>() {
    fun getAccounts(type: AccountStandardType): Sequence<Account> {
        val topAccount = this[type.id]
        if (topAccount != null) {
            return sequence {
                yieldAll(getChildren(topAccount))
            }
        }
        return emptySequence()
    }


    fun getUserAccounts(): Sequence<Account> {
        return sequenceOf(
            getAccounts(AccountStandardType.Asset),
            getAccounts(AccountStandardType.Liability)
        ).flatten()
    }

    fun favoriteOrFirstUserAccount(): Account {
        var firstAccount: Account? = null
        for (account in getUserAccounts()) {
            if (firstAccount == null) {
                firstAccount = account
            }
            if (account.extra.getOrDefault("PreferredAccount", "No").equals("Yes", false)) {
                return account
            }
        }
        if (firstAccount == null) {
            throw Exception("No user accounts")
        }
        return firstAccount
    }

    private fun getChildren(account: Account): Sequence<Account> {
        return sequence {
            for (id in account.subAccounts) {
                val child = this@Accounts[id]
                if (child != null) {
                    yield(child)
                    yieldAll(getChildren(child))
                }
            }
        }
    }


    fun isUserAccount(accountId: String): Boolean {
        val account = this[accountId] ?: return false
        val topAccount = getTopAccount(account)
        return (topAccount.id == AccountStandardType.Asset.id ||
                topAccount.id == AccountStandardType.Liability.id)
    }

    private fun getParentAccount(account: Account): Account? {
        val parentAccountId: String? = account.parentAccountId
        if (parentAccountId.isNullOrEmpty()) {
            return null
        }
        return this[parentAccountId]
    }

    private fun getTopAccount(account: Account): Account {
        val parentAccount = getParentAccount(account)
        if (parentAccount != null) {
            return getTopAccount(parentAccount)
        }
        return account
    }

//
//    fun isTransactionAccount(account: Account): Boolean {
//        val topAccount = getTopAccount(account)
//        return (topAccount.id == AccountStandardType.Income.id ||
//                topAccount.id == AccountStandardType.Expense.id)
//    }
//


}


object AccountComparable: Comparable<Account>()
