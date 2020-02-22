package eu.vmladenov.amymoney.dagger

import dagger.Component
import eu.vmladenov.amymoney.storage.xml.dagger.XmlHandlerComponent
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun getXmlHandlerComponentFactory(): XmlHandlerComponent.Factory
}
