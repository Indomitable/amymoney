package eu.vmladenov.amymoney.ui.dagger

import dagger.Subcomponent
import eu.vmladenov.amymoney.ui.views.institutions.InstitutionsViewModel
import javax.inject.Scope

@Scope
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelScope

@ViewModelScope
@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {

    fun getInstitutionsViewModel(): InstitutionsViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): ViewModelComponent
    }
}
