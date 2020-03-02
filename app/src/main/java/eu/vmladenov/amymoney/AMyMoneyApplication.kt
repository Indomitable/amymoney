package eu.vmladenov.amymoney

import android.app.Application
import eu.vmladenov.amymoney.dagger.AppComponent
import eu.vmladenov.amymoney.dagger.DaggerAppComponent
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository

class AMyMoneyApplication: Application() {
    val injector: AppComponent = DaggerAppComponent.create()
    val repository: IAMyMoneyRepository = injector.getRepository()
}
