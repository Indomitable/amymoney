package eu.vmladenov.amymoney.android_test.storage.xml

import android.util.Xml
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.intellij.lang.annotations.Language
import org.junit.After
import org.junit.runner.RunWith
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

open class BaseXmlHandlerTest {
    private lateinit var reader: StringReader

    protected fun createParser(@Language("XML") input: String): XmlPullParser {
        reader = StringReader(input)
        val parser = Xml.newPullParser().also {
            it.setInput(reader)
            it.nextTag() // When initialized XmlPullParser points to START_DOCUMENT.
        }

        return parser
    }

    @After
    fun tearDown() {
        reader.close()
    }
}
