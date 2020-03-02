package eu.vmladenov.amymoney.ui.views.institutions

import androidx.lifecycle.*
import eu.vmladenov.amymoney.AMyMoneyApplication
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Institutions
import eu.vmladenov.amymoney.ui.dagger.DaggerViewModelComponent
import eu.vmladenov.amymoney.ui.dagger.ViewModelComponent
import eu.vmladenov.amymoney.ui.views.BaseViewModelFactory
import io.reactivex.rxjava3.core.BackpressureStrategy
import java.lang.IllegalArgumentException

class InstitutionsViewModelFactory : BaseViewModelFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InstitutionsViewModel::class.java)) {
            return injector.getInstitutionsViewModel() as T
        }
        throw IllegalArgumentException("Wrong ViewModel")
    }
}

class InstitutionsViewModel(private val repository: IAMyMoneyRepository) : AndroidViewModel() {
    val institutions: LiveData<Institutions>
        get() {
            return LiveDataReactiveStreams.fromPublisher(repository.institutions.toFlowable(BackpressureStrategy.LATEST))
        }


    override fun onCleared() {
        super.onCleared()
    }
}

