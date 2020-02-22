package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.storage.xml.XmlUserHandler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class XmlUserHandlerTests: BaseXmlHandlerTest() {
    lateinit var service: XmlUserHandler

    @Before
    fun setup() {
        service = XmlUserHandler()
    }

    @Test
    //@DisplayName("Given proper user xml it should be able to read it properly")
    fun shouldBeAbleToRead() {
        val parser = createParser("""
 <USER email="user@server.com" name="Test User">
  <ADDRESS street="Landstraßer Hauptstraße 1" county="Austria" city="Wien" telephone="+43555444" zipcode="1030"/>
 </USER>
            """)
        val user = service.read(parser)
        assertEquals("user@server.com", user.email)
        assertEquals("Test User", user.name)
        assertEquals("Test User", user.name)
        assertEquals("Wien", user.address.city)
        assertEquals("Austria", user.address.country)
        assertEquals("Landstraßer Hauptstraße 1", user.address.street)
        assertEquals("+43555444", user.address.telephone)
        assertEquals("1030", user.address.postCode)
    }

}