package eu.vmladenov.amymoney

import android.app.Application
import eu.vmladenov.amymoney.dagger.AppComponent
import eu.vmladenov.amymoney.dagger.DaggerAppComponent

class AMyMoneyApplication: Application() {
    val injector: AppComponent = DaggerAppComponent.create()
}
