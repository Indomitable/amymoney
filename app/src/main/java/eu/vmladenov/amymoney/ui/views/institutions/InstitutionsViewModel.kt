package eu.vmladenov.amymoney.ui.views.institutions

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Institutions
import io.reactivex.rxjava3.core.BackpressureStrategy

class InstitutionsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        return InstitutionsViewModel(repository) as T
    }
}

class InstitutionsViewModel(private val repository: IAMyMoneyRepository) : ViewModel() {
    val institutions: LiveData<Institutions>
        get() {
            return LiveDataReactiveStreams.fromPublisher(repository.institutions.toFlowable(BackpressureStrategy.LATEST))
        }
}

