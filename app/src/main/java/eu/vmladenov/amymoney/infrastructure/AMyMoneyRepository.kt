package eu.vmladenov.amymoney.infrastructure

import eu.vmladenov.amymoney.models.*
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

interface IAMyMoneyRepository {
    var fileInfo: FileInfo
    var user: User
    val institutions: BehaviorSubject<Institutions>
    val payees: BehaviorSubject<Payees>
    val costCenters: CostCenters
    val tags: BehaviorSubject<Tags>
    val accounts: BehaviorSubject<Accounts>
    val transactions: BehaviorSubject<Transactions>
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
    override val institutions: BehaviorSubject<Institutions> = BehaviorSubject.createDefault(Institutions())
    override val payees: BehaviorSubject<Payees> = BehaviorSubject.createDefault(Payees())
    override val costCenters: CostCenters = CostCenters()
    override val tags: BehaviorSubject<Tags> = BehaviorSubject.createDefault(Tags())
    override val accounts: BehaviorSubject<Accounts> = BehaviorSubject.createDefault(Accounts())
    override val transactions: BehaviorSubject<Transactions> = BehaviorSubject.createDefault(Transactions())
    override val securities: Securities = Securities()
    override val currencies: Securities = Securities()
    override val prices: Prices = Prices()
    override var extra: Map<String, String> = emptyMap()
    override val unsupportedTags: MutableList<UnsupportedTag> = mutableListOf()

    override fun isEmpty() = accounts.value.size == 0
}
