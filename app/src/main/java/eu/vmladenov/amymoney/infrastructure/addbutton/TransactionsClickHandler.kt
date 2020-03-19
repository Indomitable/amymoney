package eu.vmladenov.amymoney.infrastructure.addbutton

import android.app.Activity
import android.view.View
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.FloatButtonRelatedActionsService
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository

class TransactionsClickHandler {
    fun handle(repository: IAMyMoneyRepository, activity: Activity, fabMain: View) {
        val relatedActionsService = FloatButtonRelatedActionsService()
        relatedActionsService.openActivities(activity, fabMain, R.layout.transactions_actions)
    }
}
