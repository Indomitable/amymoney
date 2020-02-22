package eu.vmladenov.amymoney.storage.xml.dagger

import dagger.Binds
import dagger.Module
import eu.vmladenov.amymoney.storage.xml.*

@Module
abstract class XmlReaderModule {
    @Binds
    abstract fun bindFileInfoReader(fileInfoReader: XmlFileInfoReader): IXmlFileInfoReader

    @Binds
    abstract fun bindUserReader(userReader: XmlUserReader): IXmlUserReader

    @Binds
    abstract fun bindFileReader(fileReader: XmlFileReader): IXmlFileReader
}
