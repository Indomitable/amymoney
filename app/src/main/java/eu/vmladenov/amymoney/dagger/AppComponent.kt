package eu.vmladenov.amymoney.dagger

import dagger.Component
import eu.vmladenov.amymoney.storage.xml.dagger.XmlHandlerComponent
import javax.inject.Singleton

@Component
interface AppComponent {
    fun getXmlHandlerComponentFactory(): XmlHandlerComponent.Factory
}
