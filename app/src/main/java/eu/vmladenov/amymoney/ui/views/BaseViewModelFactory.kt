package eu.vmladenov.amymoney.ui.views

import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.ui.dagger.DaggerViewModelComponent

abstract class BaseViewModelFactory: ViewModelProvider.Factory {
    val injector by lazy {
        DaggerViewModelComponent.builder().build()
    }
}
