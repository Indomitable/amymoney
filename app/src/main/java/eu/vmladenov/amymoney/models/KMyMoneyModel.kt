package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlParseException
import eu.vmladenov.amymoney.storage.xml.XmlTags

class KMyMoneyFile {
    var fileInfo: FileInfo? = null
    var user: User? = null
    var institutions: Institutions? = null
    var payees: Payees? = null
    var costCenters: CostCenters? = null
    var tags: Tags? = null
    var accounts: Accounts? = null
    var transactions: Transactions? = null
    var securities: Securities? = null
    var currencies: Securities? = null
    var prices: Prices? = null
    val unsupportedTags = mutableListOf<UnsupportedTag>()
    val extra = mutableMapOf<String, String>()

    fun model(): KMyMoneyModel {
        return KMyMoneyModel(
            fileInfo = fileInfo ?: throw XmlParseException(XmlTags.FileInfo, "No File info tag is found"),
            user = user ?: User(),
            institutions = institutions ?: Institutions(emptyList()),
            payees = payees ?: Payees(emptyList()),
            costCenters = costCenters ?: CostCenters(emptyList()),
            tags = tags ?: Tags(emptyList()),
            accounts = accounts ?: Accounts(emptyList()),
            transactions = transactions ?: Transactions(emptyList()),
            securities = securities ?: Securities(emptyList()),
            currencies = currencies ?: Securities(emptyList()),
            prices = prices ?: Prices(emptyList()),
            extra = extra,
            unsupportedTags = unsupportedTags
        )
    }
}

data class KMyMoneyModel(
    val fileInfo: FileInfo,
    val user: User,
    val institutions: Institutions,
    val payees: Payees,
    val costCenters: CostCenters,
    val tags: Tags,
    val accounts: Accounts,
    val transactions: Transactions,
    val securities: Securities,
    val currencies: Securities,
    val prices: Prices,
    val extra: Map<String, String>,
    val unsupportedTags: List<UnsupportedTag>
)
