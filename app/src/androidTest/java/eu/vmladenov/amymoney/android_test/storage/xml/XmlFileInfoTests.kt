package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.storage.xml.XmlFileInfoHandler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class XmlFileInfoTests: BaseXmlHandlerTest() {
    private lateinit var service: XmlFileInfoHandler

    @Before
    fun setup() {
        service = XmlFileInfoHandler()
    }

    @Test
    //@DisplayName("Given proper user xml it should be able to read it properly")
    fun shouldBeAbleToRead() {
        val parser = createParser(
            """
 <FILEINFO>
  <CREATION_DATE date="2020-01-20"/>
  <LAST_MODIFIED_DATE date="2020-02-22"/>
  <VERSION id="1"/>
  <FIXVERSION id="5"/>
 </FILEINFO>
            """
        )
        val fileInfo = service.read(parser)
        assertEquals(GregorianCalendar(2020, 0, 20).time, fileInfo.creationDate)
        assertEquals(GregorianCalendar(2020, 1, 22).time, fileInfo.lastModificationDate)
        assertEquals(1, fileInfo.version)
        assertEquals(5, fileInfo.fixVersion)
    }
}
