package eu.vmladenov.amymoney.infrastructure.addbutton

import android.app.Activity
import android.view.View
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.infrastructure.navigation.Destinations

//interface IAddButtonClickListenerFactory {
//    fun getOnClickListener(): View.OnClickListener
//}

class AddButtonClickListener(
    private val repository: IAMyMoneyRepository,
    private val activity: Activity
): View.OnClickListener {
    private val homeClickListener = HomeClickListener(repository, activity)

    override fun onClick(v: View) {
        when (repository.destination.value) {
            Destinations.HOME -> homeClickListener.handle()
        }
    }
}
