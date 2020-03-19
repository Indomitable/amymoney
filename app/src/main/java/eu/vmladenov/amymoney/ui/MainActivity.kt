package eu.vmladenov.amymoney.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Xml
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.*
import eu.vmladenov.amymoney.infrastructure.addbutton.AddButtonClickListener
import eu.vmladenov.amymoney.infrastructure.addbutton.HomeClickListener
import eu.vmladenov.amymoney.infrastructure.navigation.INavigationChangedListener
import eu.vmladenov.amymoney.storage.xml.IXmlFileHandler
import eu.vmladenov.amymoney.storage.xml.XmlFile
import eu.vmladenov.amymoney.ui.views.dialogs.LoadProgressFragment
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.main_view.*
import kotlinx.coroutines.*
import java.util.zip.GZIPInputStream

class MainActivity : BaseActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var repository: IAMyMoneyRepository
    private lateinit var xmlFileHandler: IXmlFileHandler
    private lateinit var navigationChangedListener: INavigationChangedListener
    private lateinit var navigationController: NavController

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)

        repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        xmlFileHandler = ServiceProvider.getService(IXmlFileHandler::class)
        navigationChangedListener = ServiceProvider.getService(INavigationChangedListener::class)

        setSupportActionBar(mainToolbar)
        repository.isEmpty()
            .takeUntil(destroyNotifier)
            .subscribe { isEmpty ->
                if (!isEmpty) {
                    initializeNavigation()
                }
            }

        fabMain.setOnClickListener(AddButtonClickListener(repository, this))
        navigationController = getNavController()
        navigationController.addOnDestinationChangedListener(navigationChangedListener)
    }

    override fun onDestroy() {
        navigationController.removeOnDestinationChangedListener(navigationChangedListener)
        super.onDestroy()
    }

/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }*/


    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // https://issuetracker.google.com/issues/142847973
    // https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set
    private fun getNavController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as? NavHostFragment
        return navHostFragment!!.navController
    }

    private fun initializeNavigation() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_institutions,
                R.id.nav_accounts,
                R.id.nav_categories,
                R.id.nav_tags,
                R.id.nav_payees,
                R.id.nav_transactions
            ),
            mainViewLayout
        )

        setupActionBarWithNavController(
            navigationController,
            appBarConfiguration
        )
        leftNavView.setupWithNavController(navigationController)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == HomeClickListener.FILE_SELECT_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFile = data.data ?: return
            loadRepository(selectedFile)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadRepository(fileUrl: Uri) {
        val reporter = ProgressReporter()
        GlobalScope.launch(Dispatchers.Main) {
            val dialog = LoadProgressFragment(reporter)
            dialog.showNow(supportFragmentManager, "progress")
            dialog.listen()
        }
        GlobalScope.launch {
            val file = readFileAsync(fileUrl, reporter).await()
            withContext(Dispatchers.Main) {
                repository.updateFromFile(file)
            }
            reporter.progress("Finish")
            reporter.close()
        }
    }

    private suspend fun readFileAsync(fileUrl: Uri, reporter: ProgressReporter): Deferred<XmlFile> = coroutineScope {
        return@coroutineScope async {
            reporter.progress("Unziping file")
            contentResolver.openInputStream(fileUrl).use { inputStream ->
                GZIPInputStream(inputStream).use { stream ->
                    val parser = Xml.newPullParser()
                    parser.setInput(stream, "utf-8")
                    reporter.progress("Processing xml file")
                    return@async xmlFileHandler.read(parser, reporter)
                }
            }
        }
    }
}
