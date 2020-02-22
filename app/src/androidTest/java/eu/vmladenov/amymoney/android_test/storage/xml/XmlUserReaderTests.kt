package eu.vmladenov.amymoney.android_test.storage.xml

import android.util.Xml
import eu.vmladenov.amymoney.storage.xml.XmlUserReader
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

import java.io.StringReader

class XmlUserReaderTests {
    lateinit var reader: StringReader

    fun create(input: String): XmlUserReader {
        reader = StringReader(input)
        val parser = Xml.newPullParser().also {
            it.setInput(reader)
            it.nextTag() // When initialized XmlPullParser points to START_DOCUMENT.
        }

        return XmlUserReader(parser)
    }

    @After
    fun tearDown() {
        reader.close()
    }

    @Test
    //@DisplayName("Given proper user xml it should be able to read it properly")
    fun shouldBeAbleToRead() {
        val service = create("""
                 <USER email="user@server.com" name="User Name">
                  <ADDRESS city="Vienna" zipcode="1010" county="Austria" telephone="+43555444" street="Landstraßer Hauptstraße 1"/>
                 </USER>
            """)
        val user = service.read()
        assertEquals("user@server.com", user.email)
    }

}