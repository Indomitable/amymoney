package eu.vmladenov.amymoney.infrastructure

import eu.vmladenov.amymoney.models.*
import eu.vmladenov.amymoney.storage.xml.XmlFile
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

interface IAMyMoneyRepository {
    val fileInfo: BehaviorSubject<FileInfo>
    val user: BehaviorSubject<User>
    val institutions: BehaviorSubject<Institutions>
    val payees: BehaviorSubject<Payees>
    val costCenters: BehaviorSubject<CostCenters>
    val tags: BehaviorSubject<Tags>
    val accounts: BehaviorSubject<Accounts>
    val categories: BehaviorSubject<Accounts>
    val transactions: BehaviorSubject<Transactions>
    val securities: BehaviorSubject<Securities>
    val currencies: BehaviorSubject<Securities>
    val prices: BehaviorSubject<Prices>
    val extra: BehaviorSubject<Map<String, String>>
    val unsupportedTags: BehaviorSubject<List<UnsupportedTag>>
    fun isEmpty(): Observable<Boolean>
    fun updateFromFile(file: XmlFile)

    val currentAccounts: Accounts
    val currentPayees: Payees
    val currentTags: Tags
}

class AMyMoneyRepository @Inject constructor() : IAMyMoneyRepository {
    override val fileInfo: BehaviorSubject<FileInfo> = BehaviorSubject.createDefault(FileInfo())
    override val user: BehaviorSubject<User> = BehaviorSubject.createDefault(User())
    override val institutions: BehaviorSubject<Institutions> = BehaviorSubject.createDefault(Institutions())
    override val payees: BehaviorSubject<Payees> = BehaviorSubject.createDefault(Payees())
    override val costCenters: BehaviorSubject<CostCenters> = BehaviorSubject.createDefault(CostCenters())
    override val tags: BehaviorSubject<Tags> = BehaviorSubject.createDefault(Tags())
    override val accounts: BehaviorSubject<Accounts> = BehaviorSubject.createDefault(Accounts())
    override val categories: BehaviorSubject<Accounts> = BehaviorSubject.createDefault(Accounts())
    override val transactions: BehaviorSubject<Transactions> = BehaviorSubject.createDefault(Transactions())
    override val securities: BehaviorSubject<Securities> = BehaviorSubject.createDefault(Securities())
    override val currencies: BehaviorSubject<Securities> = BehaviorSubject.createDefault(Securities())
    override val prices: BehaviorSubject<Prices> = BehaviorSubject.createDefault(Prices())
    override val extra: BehaviorSubject<Map<String, String>> = BehaviorSubject.createDefault(emptyMap())
    override val unsupportedTags: BehaviorSubject<List<UnsupportedTag>> = BehaviorSubject.createDefault(emptyList())

    override val currentAccounts: Accounts
        get() = accounts.value

    override val currentPayees: Payees
        get() = payees.value

    override val currentTags: Tags
        get() = tags.value

    override fun isEmpty(): Observable<Boolean> = accounts.map { a -> a.values.isEmpty() }

    override fun updateFromFile(file: XmlFile) {
        fileInfo.onNext(file.fileInfo)
        user.onNext(file.user)
        institutions.onNext(file.institutions)
        payees.onNext(file.payees)
        costCenters.onNext(file.costCenters)
        tags.onNext(file.tags)
        accounts.onNext(getUserAccounts(file.accounts))
        categories.onNext(getCategories(file.accounts))
        transactions.onNext(file.transactions)
        securities.onNext(file.securities)
        currencies.onNext(file.currencies)
        prices.onNext(file.prices)
        extra.onNext(file.extra)
        unsupportedTags.onNext(file.unsupportedTags)
    }

    private fun getUserAccounts(accounts: Map<String, XmlAccount>): Accounts {
        val result = Accounts()
        val accountsMap = sequenceOf(
            getAccounts(accounts, AccountStandardType.Asset).map { x -> createAccount(x, AccountStandardType.Asset) },
            getAccounts(accounts, AccountStandardType.Liability).map { x -> createAccount(x, AccountStandardType.Liability) }
        ).flatten().associateBy { a -> a.id }
        result.fill(accountsMap)
        return result
    }

    private fun getCategories(accounts: Map<String, XmlAccount>): Accounts {
        val result = Accounts()
        val accountsMap = sequenceOf(
            getAccounts(accounts, AccountStandardType.Income).map { x -> createAccount(x, AccountStandardType.Income) },
            getAccounts(accounts, AccountStandardType.Expense).map { x -> createAccount(x, AccountStandardType.Expense) }
        ).flatten().associateBy { a -> a.id }
        result.fill(accountsMap)
        return result
    }

    private fun getAccounts(accounts: Map<String, XmlAccount>, type: AccountStandardType): Sequence<XmlAccount> {
        val topAccount = accounts[type.id]
        if (topAccount != null) {
            return sequence {
                yield(topAccount)
                yieldAll(getChildren(accounts, topAccount))
            }
        }
        return emptySequence()
    }

    private fun getChildren(accounts: Map<String, XmlAccount>, account: XmlAccount): Sequence<XmlAccount> {
        return sequence {
            for (id in account.subAccounts) {
                val child = accounts[id]
                if (child != null) {
                    yield(child)
                    yieldAll(getChildren(accounts, child))
                }
            }
        }
    }
}
