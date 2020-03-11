package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.lang.Exception
import java.util.*

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
class Accounts(items: Map<String, Account> = emptyMap()) : BaseMap<Account>(items) {
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


//    fun getTopUserAccounts(): Sequence<Account> {
//        return sequence {
//            for (topAccount in tree) {
//                if (isUserAccount(topAccount.value)) {
//                    yield(topAccount.value)
//                }
//            }
//        }
//    }
//
//    fun getUserAccounts(): Sequence<Account> {
//        return sequence {
//            for (topAccount in getTopUserAccounts()) {
//                y
//            }
//        }
//    }
//
//
//    private fun buildTree(accounts: Map<String, Account>): Map<String, Account> {
//        val topItems = mutableMapOf<String, Account>()
//        for (account in accounts.values) {
//            if (account.isAccountStandardType()) {
//                account.parent = null
//                account.children.putAll(getChildren(account, accounts))
//                topItems[account.id] = account
//            }
//        }
//        return topItems
//    }
//
//    private fun getChildren(account: Account, accounts: Map<String, Account>): Sequence<Pair<String, Account>> {
//        return sequence {
//            for (childId in account.subAccounts) {
//                val child: Account = accounts[childId] ?: throw Exception("Sub account not found")
//                child.parent = account
//                child.children.putAll(getChildren(child, accounts))
//                yield(child.id to child)
//            }
//        }
//    }

//    fun isUserAccount(account: Account): Boolean {
//        val topAccount = getTopAccount(account)
//        return (topAccount.id == AccountStandardType.Asset.id ||
//            topAccount.id == AccountStandardType.Liability.id)
//    }
//
//    fun isTransactionAccount(account: Account): Boolean {
//        val topAccount = getTopAccount(account)
//        return (topAccount.id == AccountStandardType.Income.id ||
//                topAccount.id == AccountStandardType.Expense.id)
//    }
//
//    private fun getParentAccount(account: Account): Account? {
//        val parentAccountId: String? = account.parentAccountId
//        if (parentAccountId.isNullOrEmpty()) {
//            return null
//        }
//        return this[parentAccountId]
//    }
//
//    fun getTopAccount(account: Account): Account {
//        val parentAccount = getParentAccount(account)
//        return if (parentAccount != null) {
//            getTopAccount(parentAccount)
//        } else {
//            account
//        }
//    }

}


object AccountComparable: Comparable<Account>()
