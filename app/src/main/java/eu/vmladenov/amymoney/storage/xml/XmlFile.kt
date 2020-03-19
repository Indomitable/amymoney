package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*

class XmlFile {
    var fileInfo: FileInfo = FileInfo()
    var user: User = User()
    val institutions: Institutions = Institutions()
    val payees: Payees = Payees()
    val costCenters: CostCenters = CostCenters()
    val tags: Tags = Tags()
    var accounts: Map<String, XmlAccount> = emptyMap()
    val transactions: Transactions = Transactions()
    val securities: Securities = Securities()
    val currencies: Securities = Securities()
    val prices: Prices = Prices()
    val extra: MutableMap<String, String> = mutableMapOf()
    val unsupportedTags: MutableList<UnsupportedTag> = mutableListOf()
}
