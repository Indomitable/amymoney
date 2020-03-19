package eu.vmladenov.amymoney.infrastructure.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import javax.inject.Inject

interface INavigationChangedListener: NavController.OnDestinationChangedListener {
}

class NavigationChangedListener @Inject constructor(val repository: IAMyMoneyRepository):
    INavigationChangedListener {
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination, arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.nav_home -> repository.destination.onNext(Destinations.HOME)
            R.id.nav_institutions -> repository.destination.onNext(Destinations.INSTITUTIONS)
            R.id.nav_accounts -> repository.destination.onNext(Destinations.ACCOUNTS)
            R.id.nav_categories -> repository.destination.onNext(Destinations.CATEGORIES)
            R.id.nav_tags -> repository.destination.onNext(Destinations.TAGS)
            R.id.nav_payees -> repository.destination.onNext(Destinations.PAYEES)
            R.id.nav_transactions -> repository.destination.onNext(Destinations.LEDGERS)
            else -> throw Exception("Unknown destination")
        }
    }
}
