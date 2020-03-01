package eu.vmladenov.amymoney.ui

import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.models.Transaction
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.content.*
import kotlinx.android.synthetic.main.main_view.*
import java.lang.Exception
import java.util.*

class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)
        setSupportActionBar(mainToolbar)


        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_institutions),
            mainViewLayout
        )

        val navigationController = findNavController(R.id.main_nav_host_fragment)
        setupActionBarWithNavController(
            navigationController,
            appBarConfiguration
        )
        leftNavView.setupWithNavController(navigationController)

//        supportActionBar!!.title = ""
//        mainActivityToolbar.setSubtitle("Test")
//        mainActivityToolbar.inflateMenu(R.menu.main_menu)
//
//        val ammount = displayAmmount()
//        lblEmptyState.text = ammount.toDecimal().toString()


    }

/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }*/

    fun displayAmmount(): Fraction {
        val model = application.state.model
        val amount = model.transactions
            .filter { t -> t.postDate!! < GregorianCalendar().time && t.splits.any { s -> s.accountId == "A000001" } }
            .fold(Fraction(0, 1)) { acc: Fraction, transaction: Transaction ->
                val split = transaction.splits.find { it.accountId == "A000001" }!!
                val res = (acc + split.value) // We need to simplify because during the process the denominator is growing too much and creates overflow
                if (res.denominator == 0L) {
                    throw Exception("Denominator is zero")
                }
                return@fold res
            }
        return amount
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
