package eu.vmladenov.amymoney.infrastructure.addbutton

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.FloatButtonRelatedActionsService
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.ui.MainActivity
import eu.vmladenov.amymoney.ui.views.transactions.TransactionsFragmentDirections
import kotlinx.android.synthetic.main.transactions_actions.view.*

class TransactionsClickHandler {
    fun handle(repository: IAMyMoneyRepository, activity: MainActivity, fabMain: View) {
        val relatedActionsService = FloatButtonRelatedActionsService()
        val view = relatedActionsService.openActivities(activity, fabMain, R.layout.transactions_actions)
        val depositButton = view.deposit_button
        depositButton.setOnClickListener {
            val navController = activity.getNavController()
            val direction = TransactionsFragmentDirections.actionNavTransactionsToNavTransactionsEdit(null)
            navController.navigate(direction)
        }
    }
}
