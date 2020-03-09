package eu.vmladenov.amymoney.ui.views

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import eu.vmladenov.amymoney.R

open class NavigationFragment: DisposableFragment() {
    // https://issuetracker.google.com/issues/142847973
    // https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set
    protected fun getNavController(): NavController {
        val navHostFragment = this.requireActivity().supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as? NavHostFragment
        return navHostFragment!!.navController
    }
}
