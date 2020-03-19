package eu.vmladenov.amymoney.dagger

import dagger.Binds
import dagger.Component
import dagger.Module
import eu.vmladenov.amymoney.infrastructure.AMyMoneyRepository
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.infrastructure.navigation.INavigationChangedListener
import eu.vmladenov.amymoney.infrastructure.navigation.NavigationChangedListener
import eu.vmladenov.amymoney.storage.xml.dagger.XmlHandlerComponent
import eu.vmladenov.amymoney.ui.dagger.UiComponent
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

    @Binds
    @AppScope
    abstract fun bindNavigationChangedListener(navigationChangedListener: NavigationChangedListener): INavigationChangedListener
}

@AppScope
@Component(modules = [AppModule::class])
abstract class AppComponent {

    abstract fun getXmlHandlerComponentFactory(): XmlHandlerComponent.Factory

    abstract fun getUiComponentFactory(): UiComponent.Factory

    abstract fun getRepository(): IAMyMoneyRepository

    abstract fun getNavigationChangedListener(): INavigationChangedListener
}
