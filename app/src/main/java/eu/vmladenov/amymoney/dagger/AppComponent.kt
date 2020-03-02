package eu.vmladenov.amymoney.dagger

import dagger.Binds
import dagger.Component
import dagger.Module
import eu.vmladenov.amymoney.infrastructure.AMyMoneyRepository
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.storage.xml.dagger.XmlHandlerComponent
import javax.inject.Scope

@Scope
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@Module(subcomponents = [XmlHandlerComponent::class])
internal abstract class AppModule {

    @Binds
    @AppScope
    abstract fun bindRepository(repository: AMyMoneyRepository): IAMyMoneyRepository
}

@AppScope
@Component(modules = [AppModule::class])
abstract class AppComponent {
    val xmlHandlerComponent: Lazy<XmlHandlerComponent> = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getXmlHandlerComponentFactory().create()
    }

    abstract fun getXmlHandlerComponentFactory(): XmlHandlerComponent.Factory

    abstract fun getRepository(): IAMyMoneyRepository
}
