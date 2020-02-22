package eu.vmladenov.amymoney

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Xml
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import eu.vmladenov.amymoney.storage.xml.XmlFileReader
import eu.vmladenov.amymoney.storage.xml.dagger.XmlReaderComponent
import java.util.zip.GZIPInputStream

class MainActivity : AppCompatActivity() {
    private val FILE_SELECT_REQUEST = 1001
    private lateinit var xmlReaderFactory: XmlReaderComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        xmlReaderFactory = (applicationContext as AMyMoneyApplication).injector.getXmlReaderComponentFactory()
        val button = findViewById<Button>(R.id.openFile)
        button.setOnClickListener {
            val intent = Intent().setType("*/*").setAction(Intent.ACTION_OPEN_DOCUMENT)
            startActivityForResult(Intent.createChooser(intent, "Select file"), FILE_SELECT_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val contentResolver = applicationContext.contentResolver
        if (requestCode == FILE_SELECT_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedfile = data.data ?: return
            contentResolver.openInputStream(selectedfile).use { inputStream ->
                GZIPInputStream(inputStream).use { stream ->
                    val parser = Xml.newPullParser()
                    parser.setInput(stream, "utf-8")
                    val reader = xmlReaderFactory.create(parser)
                    val file = reader.getXmlFileReader().read()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
