package eu.vmladenov.amymoney.ui

import androidx.appcompat.app.AppCompatActivity
import eu.vmladenov.amymoney.AMyMoneyApplication
import eu.vmladenov.amymoney.dagger.AppComponent

abstract class BaseActivity protected constructor(): AppCompatActivity() {
    protected val application: AMyMoneyApplication
        get() = (applicationContext as AMyMoneyApplication)
}
