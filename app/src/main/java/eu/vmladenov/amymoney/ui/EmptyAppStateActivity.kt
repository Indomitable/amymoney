package eu.vmladenov.amymoney.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Xml
import eu.vmladenov.amymoney.MainActivity
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.storage.xml.IXmlFileHandler
import kotlinx.android.synthetic.main.activity_empty_app_state.*
import java.util.zip.GZIPInputStream


class EmptyAppStateActivity : BaseActivity() {

    private val FILE_SELECT_REQUEST = 1001
    private lateinit var xmlHandler: IXmlFileHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (application.state.isInitialized) {
            redirectToMain()
            return
        }
        setContentView(R.layout.activity_empty_app_state)

        xmlHandler = injector.xmlHandlerComponent.value.getXmlFileReader()
        addDataSource.setOnClickListener {
            val intent = Intent().setType("*/*").setAction(Intent.ACTION_OPEN_DOCUMENT)
            startActivityForResult(Intent.createChooser(intent, "Select file"), FILE_SELECT_REQUEST)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("AMYMONEY","RESTORED")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val contentResolver = applicationContext.contentResolver
        if (requestCode == FILE_SELECT_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFile = data.data ?: return
            contentResolver.openInputStream(selectedFile).use { inputStream ->
                GZIPInputStream(inputStream).use { stream ->
                    val parser = Xml.newPullParser()
                    parser.setInput(stream, "utf-8")
                    application.state.model = xmlHandler.read(parser)
                    redirectToMain()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun redirectToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
