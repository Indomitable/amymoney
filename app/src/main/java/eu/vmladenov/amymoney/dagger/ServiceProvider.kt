package eu.vmladenov.amymoney.dagger

import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.infrastructure.navigation.INavigationChangedListener
import eu.vmladenov.amymoney.storage.xml.IXmlFileHandler
import eu.vmladenov.amymoney.ui.views.transactions.ITransactionViewModelFactory
import kotlin.reflect.KClass

object ServiceProvider {
    private val injector: AppComponent = DaggerAppComponent.create()

    private val xmlHandlerComponent by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        injector.getXmlHandlerComponentFactory().create()
    }

    private val uiComponent by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        injector.getUiComponentFactory().create()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> getService(service: KClass<T>): T {
        if (service == IAMyMoneyRepository::class) {
            return injector.getRepository() as T
        }
        if (service == INavigationChangedListener::class) {
            return injector.getNavigationChangedListener() as T
        }
        if (service == IXmlFileHandler::class) {
            return xmlHandlerComponent.getXmlFileReader() as T
        }
        if (service == ITransactionViewModelFactory::class) {
            return uiComponent.getTransactionViewModelFactory() as T
        }
        throw Exception("Not supported")
    }
}
