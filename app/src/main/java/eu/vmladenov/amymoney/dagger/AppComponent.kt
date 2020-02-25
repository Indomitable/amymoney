package eu.vmladenov.amymoney.dagger

import dagger.Component
import dagger.Module
import eu.vmladenov.amymoney.storage.xml.IXmlFileHandler
import eu.vmladenov.amymoney.storage.xml.dagger.XmlHandlerComponent
import javax.inject.Singleton

//@Module(subcomponents = [XmlHandlerComponent::class])
//internal class AppModule

@Component // (modules = [AppModule::class])
abstract class AppComponent {
    val xmlHandlerComponent: Lazy<XmlHandlerComponent> = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getXmlHandlerComponentFactory().create()
    }

    abstract fun getXmlHandlerComponentFactory(): XmlHandlerComponent.Factory
}
