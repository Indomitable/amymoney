package eu.vmladenov.amymoney.local_test

import eu.vmladenov.amymoney.dagger.DaggerAppComponent
import eu.vmladenov.amymoney.storage.xml.XmlFileHandler
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*


class DependencyInjectionTests {
    @Test
    @DisplayName("Should create singletons")
    fun shouldProvideSingletons() {
        val appComponent = DaggerAppComponent.create()
        val component0 = appComponent.xmlHandlerComponent
        val component1 = appComponent.xmlHandlerComponent

        assertSame(component0, component1)

        val service0 = component0.value.getXmlFileReader() as XmlFileHandler
        val service1 = component1.value.getXmlFileReader() as XmlFileHandler

        assertSame(service0, service1)

        for (key in service0.handlers.keys) {
            val handler0 = service0.handlers[key]
            val handler1 = service1.handlers[key]
            assertSame(handler0, handler1)
        }
    }
}
