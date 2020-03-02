package eu.vmladenov.amymoney.ui.views.institutions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveDataReactiveStreams
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Institutions
import io.reactivex.rxjava3.core.BackpressureStrategy


class InstitutionsViewModel(private val repository: IAMyMoneyRepository) : ViewModel() {
    val institutions: LiveData<Institutions>
        get() {
            return LiveDataReactiveStreams.fromPublisher(repository.institutions.toFlowable(BackpressureStrategy.LATEST))
        }


    override fun onCleared() {
        super.onCleared()
    }
}
