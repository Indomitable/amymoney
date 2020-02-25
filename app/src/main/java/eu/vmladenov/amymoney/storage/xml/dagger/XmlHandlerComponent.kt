package eu.vmladenov.amymoney.storage.xml.dagger

import dagger.Subcomponent
import eu.vmladenov.amymoney.storage.xml.IXmlFileHandler
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [XmlHandlerModule::class] )
interface XmlHandlerComponent {

    fun getXmlFileReader(): IXmlFileHandler

    @Subcomponent.Factory
    interface Factory {
        fun create(): XmlHandlerComponent
    }
}
