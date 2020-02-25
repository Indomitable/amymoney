package eu.vmladenov.amymoney.storage.xml.dagger

import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import eu.vmladenov.amymoney.storage.xml.*

@MapKey
annotation class XmlTagsKey(val value: XmlTags)

@Module
abstract class XmlHandlerModule {
    @Binds
    abstract fun bindFileHandler(fileHandler: XmlFileHandler): IXmlFileHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.FileInfo)
    abstract fun bindFileInfoHandler(institutionsHandler: XmlFileInfoHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.User)
    abstract fun bindUserHandler(institutionsHandler: XmlUserHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.Institutions)
    abstract fun bindInstitutionsHandler(institutionsHandler: XmlInstitutionsHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.Payees)
    abstract fun bindPayeesHandler(payeesHandler: XmlPayeesHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.CostCenters)
    abstract fun bindCostCentersHandler(costCentersHandler: XmlCostCentersHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.Tags)
    abstract fun bindTagsHandler(tagsHandler: XmlTagsHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.Accounts)
    abstract fun bindAccountsHandler(accountHandler: XmlAccountHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.Transactions)
    abstract fun bindTransactionsHandler(transactionsHandler: XmlTransactionsHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.Securities)
    abstract fun bindSecuritiesHandler(securitiesHandler: XmlSecuritiesHandler): IXmlFileTagHandler

    @Binds
    @IntoMap
    @XmlTagsKey(XmlTags.Currencies)
    abstract fun bindCurrenciesHandler(currenciesHandler: XmlCurrenciesHandler): IXmlFileTagHandler
}
