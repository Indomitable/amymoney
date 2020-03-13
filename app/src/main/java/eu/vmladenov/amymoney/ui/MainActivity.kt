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
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.storage.xml.IXmlFileHandler
import eu.vmladenov.amymoney.storage.xml.XmlFile
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.main_view.*
import kotlinx.coroutines.*
import java.util.zip.GZIPInputStream


class MainActivity : BaseActivity() {
    private val FILE_SELECT_REQUEST = 1001
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var repository: IAMyMoneyRepository
    private lateinit var xmlFileHandler: IXmlFileHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)
        setSupportActionBar(mainToolbar)
        initializeNav()
        initFloatButton()

        repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        xmlFileHandler = ServiceProvider.getService(IXmlFileHandler::class)
    }

/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = getNavController()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // https://issuetracker.google.com/issues/142847973
    // https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set
    private fun getNavController(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as? NavHostFragment
        return navHostFragment!!.navController
    }

    private fun initializeNav() {
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

        val navigationController = getNavController()
        setupActionBarWithNavController(
            navigationController,
            appBarConfiguration
        )
        leftNavView.setupWithNavController(navigationController)
    }

    private fun initFloatButton() {
        fabMain.setOnClickListener {
            if (repository.isEmpty()) {
                val intent = Intent().setType("*/*").setAction(Intent.ACTION_OPEN_DOCUMENT)
                startActivityForResult(Intent.createChooser(intent, "Select file"), FILE_SELECT_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_SELECT_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFile = data.data ?: return
            GlobalScope.launch(Dispatchers.Main) {
                fillRepo(selectedFile)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun readFileAsync(fileUrl: Uri): Deferred<XmlFile> {
        return GlobalScope.async {
            contentResolver.openInputStream(fileUrl).use { inputStream ->
                GZIPInputStream(inputStream).use { stream ->
                    val parser = Xml.newPullParser()
                    parser.setInput(stream, "utf-8")
                    return@async xmlFileHandler.read(parser)
                }
            }
        }
    }

    private suspend fun fillRepo(fileUrl: Uri) {
        val file = readFileAsync(fileUrl).await()
        repository.updateFromFile(file)
    }
}
