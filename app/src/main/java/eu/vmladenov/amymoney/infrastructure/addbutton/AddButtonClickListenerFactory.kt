package eu.vmladenov.amymoney.infrastructure.addbutton

import android.view.View
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.infrastructure.navigation.Destinations
import eu.vmladenov.amymoney.ui.MainActivity

//interface IAddButtonClickListenerFactory {
//    fun getOnClickListener(): View.OnClickListener
//}

class AddButtonClickListener(
    private val repository: IAMyMoneyRepository,
    private val activity: MainActivity
): View.OnClickListener {
    private val homeClickListener = HomeClickHandler()
    private val transactionsClickListener = TransactionsClickHandler()


    override fun onClick(fabMain: View) {
        when (repository.destination.value) {
            Destinations.HOME -> homeClickListener.handle(repository, activity)
            Destinations.LEDGERS -> transactionsClickListener.handle(repository, activity, fabMain)
        }
    }
}
