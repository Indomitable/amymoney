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
}
