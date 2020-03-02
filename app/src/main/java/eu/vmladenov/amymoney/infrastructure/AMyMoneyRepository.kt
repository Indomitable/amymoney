package eu.vmladenov.amymoney.infrastructure

import eu.vmladenov.amymoney.models.*
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

interface IAMyMoneyRepository {
    var fileInfo: FileInfo
    var user: User
    val institutions: BehaviorSubject<Institutions>
    val payees: Payees
    val costCenters: CostCenters
    val tags: Tags
    val accounts: Accounts
    val transactions: Transactions
    val securities: Securities
    val currencies: Securities
    val prices: Prices
    var extra: Map<String, String>
    val unsupportedTags: MutableList<UnsupportedTag>
    fun isEmpty(): Boolean
}

class AMyMoneyRepository @Inject constructor() : IAMyMoneyRepository {
    override var fileInfo: FileInfo = FileInfo()
    override var user: User = User()
    override val institutions: BehaviorSubject<Institutions> = BehaviorSubject.createDefault<Institutions>(Institutions())
    override val payees: Payees = Payees()
    override val costCenters: CostCenters = CostCenters()
    override val tags: Tags = Tags()
    override val accounts: Accounts = Accounts()
    override val transactions: Transactions = Transactions()
    override val securities: Securities = Securities()
    override val currencies: Securities = Securities()
    override val prices: Prices = Prices()
    override var extra: Map<String, String> = emptyMap()
    override val unsupportedTags: MutableList<UnsupportedTag> = mutableListOf()

    override fun isEmpty() = accounts.size == 0
}
