package eu.vmladenov.amymoney.storage.xml.dagger

import dagger.Binds
import dagger.Module
import eu.vmladenov.amymoney.storage.xml.*

@Module
abstract class XmlHandlerModule {
    @Binds
    abstract fun bindFileHandler(fileReader: XmlFileHandler): IXmlFileHandler

    @Binds
    abstract fun bindFileInfoHandler(fileInfoReader: XmlFileInfoHandler): IXmlFileInfoHandler

    @Binds
    abstract fun bindUserHandler(userReader: XmlUserHandler): IXmlUserHandler

    @Binds
    abstract fun bindInstitutionsHandler(institutionsHandler: XmlInstitutionsHandler): IXmlInstitutionsHandler

    @Binds
    abstract fun bindPayeesHandler(payeesHandler: XmlPayeesHandler): IXmlPayeesHandler

    @Binds
    abstract fun bindCostCentersHandler(costCentersHandler: XmlCostCentersHandler): IXmlCostCentersHandler

    @Binds
    abstract fun bindTagsHandler(tagsHandler: XmlTagsHandler): IXmlTagsHandler

    @Binds
    abstract fun bindAccountsHandler(accountHandler: XmlAccountHandler): IXmlAccountHandler

    @Binds
    abstract fun bindTransactionsHandler(transactionsHandler: XmlTransactionsHandler): IXmlTransactionsHandler
}
