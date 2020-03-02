package eu.vmladenov.amymoney.ui.dagger

import dagger.Module
import dagger.Provides
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.ui.views.institutions.InstitutionsViewModel

@Module
class ViewModelModule {
    @Provides
    fun provideInstitutionsViewModel(repository: IAMyMoneyRepository): InstitutionsViewModel {
        return InstitutionsViewModel(repository)
    }
}
