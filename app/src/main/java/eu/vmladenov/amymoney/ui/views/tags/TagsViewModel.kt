package eu.vmladenov.amymoney.ui.views.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Tag
import io.reactivex.rxjava3.core.Observable

class TagsViewModel(private val repository: IAMyMoneyRepository) : ViewModel() {
    val tags: Observable<List<Tag>>
        get() {
            return repository.tags
                .map {
                    it.values.sortedBy { t -> t.name }
                }
        }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
            return TagsViewModel(repository) as T
        }
    }
}
