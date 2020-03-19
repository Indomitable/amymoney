package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.util.*

enum class AccountStandardType(val id: String) {
    Liability("AStd::Liability"),
    Asset("AStd::Asset"),
    Expense("AStd::Expense"),
    Income("AStd::Income"),
    Equity("AStd::Equity");
}

enum class AccountType(val type: Int) {
    Unknown(0),

    /**< For error handling */
    Checkings(1),

    /**< Standard checking account */
    Savings(2),

    /**< Typical savings account */
    Cash(3),

    /**< Denotes a shoe-box or pillowcase stuffed with cash */
    CreditCard(4),

    /**< Credit card accounts */
    Loan(5),

    /**< Loan and mortgage accounts (liability) */
    CertificateDep(6),

    /**< Certificates of Deposit */
    Investment(7),

    /**< Investment account */
    MoneyMarket(8),

    /**< Money Market Account */
    Asset(9),

    /**< Denotes a generic asset account.*/
    Liability(10),

    /**< Denotes a generic liability account.*/
    Currency(11),

    /**< Denotes a currency trading account. */
    Income(12),

    /**< Denotes an income account */
    Expense(13),

    /**< Denotes an expense account */
    AssetLoan(14),

    /**< Denotes a loan (asset of the owner of this object) */
    Stock(15),

    /**< Denotes an security account as sub-account for an investment */
    Equity(16);

    /**< Denotes an equity account e.g. opening/closing balance */

    companion object {
        private val map = values().associateBy(AccountType::type)

        operator fun get(type: Int): AccountType {
            return map.getOrElse(type) { Unknown }
        }
    }
}


@XmlTag(XmlTags.Account)
data class XmlAccount(
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
) : IModel

data class Account(
    override val id: String,
    val name: String,
    val currencyId: String,
    val type: AccountType,
    val lastReconciliationDate: Date?,
    val lastModified: Date?,
    val parentAccountId: String,
    val openingDate: Date?,
    val institutionId: String,
    val number: String,
    val description: String,
    val subAccounts: List<String>,
    val extra: Map<String, String>,
    val accountType: AccountStandardType
) : IModel {
    fun isTopAccount(): Boolean {
        return AccountStandardType.values().map { it.id }.any { it == id }
    }
}

@XmlTag(XmlTags.Accounts)
class Accounts : BaseMap<Account>() {

    fun favoriteOrFirst(): Account {
        var firstAccount: Account? = null
        for (account in values) {
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

    fun getAccountsForType(accountType: AccountStandardType): List<Account> {
        /*val topAccount = this[accountType.id]
        if (topAccount != null) {
            return getChildren(topAccount)
        }
        return emptySequence()*/
        // We are going to use a filter. We don't work with big dataset, if accounts are many , then we can go from the top to children
        return this.values.filter { a -> a.accountType === accountType }
    }


//    private fun getChildren(account: Account): Sequence<Account> {
//        return sequence {
//            for (id in account.subAccounts) {
//                val child = this@Accounts[id]
//                if (child != null) {
//                    yield(child)
//                    yieldAll(getChildren(child))
//                }
//            }
//        }
//    }
}

object AccountComparable : Comparable<Account>()

fun createAccount(xmlAccount: XmlAccount, accountType: AccountStandardType): Account {
    return Account(
        id = xmlAccount.id,
        name = xmlAccount.name,
        currencyId = xmlAccount.currencyId,
        type = xmlAccount.type,
        lastReconciliationDate = xmlAccount.lastReconciliationDate,
        lastModified = xmlAccount.lastModified,
        parentAccountId = xmlAccount.parentAccountId,
        openingDate = xmlAccount.openingDate,
        institutionId = xmlAccount.institutionId,
        number = xmlAccount.number,
        description = xmlAccount.description,
        subAccounts = xmlAccount.subAccounts,
        extra = xmlAccount.extra,
        accountType = accountType
    )
}
