package eu.vmladenov.amymoney.storage.xml.dagger

import dagger.BindsInstance
import dagger.Subcomponent
import eu.vmladenov.amymoney.storage.xml.IXmlFileReader
import org.xmlpull.v1.XmlPullParser

@Subcomponent(modules = [XmlReaderModule::class] )
interface XmlReaderComponent {

    fun getXmlFileReader(): IXmlFileReader

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance parser: XmlPullParser): XmlReaderComponent
    }
}
