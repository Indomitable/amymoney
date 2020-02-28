package eu.vmladenov.amymoney.ui

import androidx.appcompat.app.AppCompatActivity
import eu.vmladenov.amymoney.AMyMoneyApplication
import eu.vmladenov.amymoney.dagger.AppComponent
import eu.vmladenov.amymoney.state.ApplicationState

abstract class BaseActivity protected constructor(): AppCompatActivity() {
    protected val application: AMyMoneyApplication
        get() = (applicationContext as AMyMoneyApplication)

    protected val injector: AppComponent
        get() = application.injector

    protected val state: ApplicationState
        get() = application.state
}