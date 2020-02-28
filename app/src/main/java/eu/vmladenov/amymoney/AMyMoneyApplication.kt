package eu.vmladenov.amymoney

import android.app.Application
import eu.vmladenov.amymoney.dagger.AppComponent
import eu.vmladenov.amymoney.dagger.DaggerAppComponent
import eu.vmladenov.amymoney.state.ApplicationState

class AMyMoneyApplication: Application() {
    val injector: AppComponent = DaggerAppComponent.create()
    val state: ApplicationState = ApplicationState()
}
