package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.util.*

enum class AccountStandardType(val id: String) {
    Liability("AStd::Liability"),
    Asset("AStd::Asset"),
    Expense("AStd::Expense"),
    Income("AStd::Income"),
    Equity("AStd::Equity")
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
    @XmlAttribute("id") val id: String,
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
    val extra: List<Pair<String, String>>
)

@XmlTag(XmlTags.Accounts)
@XmlCollection(Account::class)
data class Accounts(val accounts: List<Account>) : ArrayList<Account>(accounts)
