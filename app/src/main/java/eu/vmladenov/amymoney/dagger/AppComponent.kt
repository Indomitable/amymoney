package eu.vmladenov.amymoney.dagger

import dagger.Component
import eu.vmladenov.amymoney.storage.xml.dagger.XmlReaderComponent
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun getXmlReaderComponentFactory(): XmlReaderComponent.Factory
}
