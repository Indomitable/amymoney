package eu.vmladenov.amymoney.ui.dagger

import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import eu.vmladenov.amymoney.ui.views.transactions.ITransactionViewModelFactory
import eu.vmladenov.amymoney.ui.views.transactions.TransactionViewModelFactory
import javax.inject.Scope

@Scope
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope

@Module
abstract class UiModule {
    @Binds
    @ViewModelScope
    abstract fun bindTransactionViewModelFactory(transactionViewModelFactory: TransactionViewModelFactory): ITransactionViewModelFactory
}

@ViewModelScope
@Subcomponent(modules = [UiModule::class] )
abstract class UiComponent {

    abstract fun getTransactionViewModelFactory(): ITransactionViewModelFactory

    @Subcomponent.Factory
    interface Factory {
        fun create(): UiComponent
    }
}
