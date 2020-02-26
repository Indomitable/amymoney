package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.storage.xml.XmlInstitutionsHandler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class XmlInstitutionHandlerTests: BaseXmlHandlerTest() {
    private lateinit var service: XmlInstitutionsHandler

    @Before
    fun setup() {
        service = XmlInstitutionsHandler()
    }

    @Test
    //@DisplayName("Given proper user xml it should be able to read it properly")
    fun shouldBeAbleToRead() {
        val parser = createParser(
            """
    <INSTITUTIONS count="2">
      <INSTITUTION id="I000002" sortcode="1" manager="Manager Name" name="Bank 0">
       <ADDRESS street="Somewhere" zip="1010" city="Wien" telephone="+431111"/>
       <ACCOUNTIDS>
        <ACCOUNTID id="A000135"/>
        <ACCOUNTID id="A000136"/>
       </ACCOUNTIDS>
       <KEYVALUEPAIRS>
        <PAIR value="BICNUM123" key="bic"/>
        <PAIR value="bank-url" key="url"/>
       </KEYVALUEPAIRS>
      </INSTITUTION>
  </INSTITUTIONS>
            """
        )
        val institutions = service.read(parser)
        assertEquals(1, institutions.size)
        val institution = institutions[0]
        assertEquals("I000002", institution.id)
        assertEquals("1", institution.sortCode)
        assertEquals("Manager Name", institution.manager)
        assertEquals("Bank 0", institution.name)

        assertEquals("Somewhere", institution.address.street)
        assertEquals("1010", institution.address.postCode)
        assertEquals("Wien", institution.address.city)
        assertEquals("+431111", institution.address.telephone)

        assertEquals(2, institution.accountIds.size)
        assertEquals("A000135", institution.accountIds[0])
        assertEquals("A000136", institution.accountIds[1])

        assertEquals(2, institution.extra.size)
        assertEquals("BICNUM123", institution.extra["bic"])
        assertEquals("bank-url", institution.extra["url"])
    }

    @Test
    fun shouldBeAbleToReadWhenNoKeyValuePairs() {
        val parser = createParser(
            """
    <INSTITUTIONS count="2">
      <INSTITUTION id="I000002" sortcode="1" manager="Manager Name" name="Bank 0">
       <ADDRESS street="Somewhere" zip="1010" city="Wien" telephone="+431111"/>
       <ACCOUNTIDS>
        <ACCOUNTID id="A000135"/>
        <ACCOUNTID id="A000136"/>
       </ACCOUNTIDS>
      </INSTITUTION>
  </INSTITUTIONS>
            """
        )
        val institutions = service.read(parser)
        assertEquals(1, institutions.size)
        assertEquals(0, institutions[0].extra.size)
    }

    @Test
    fun shouldBeAbleToReadWhenNOAccountIds() {
        val parser = createParser(
            """
    <INSTITUTIONS count="2">
      <INSTITUTION id="I000002" sortcode="1" manager="Manager Name" name="Bank 0">
       <ADDRESS street="Somewhere" zip="1010" city="Wien" telephone="+431111"/>
       <ACCOUNTIDS/>       
      </INSTITUTION>
  </INSTITUTIONS>
            """
        )
        val institutions = service.read(parser)
        assertEquals(1, institutions.size)
        assertEquals(0, institutions[0].accountIds.size)
        assertEquals(0, institutions[0].extra.size)
    }

    @Test
    fun shouldBeAbleToReadMultipleInstitutions() {
        val parser = createParser(
            """
    <INSTITUTIONS count="2">
      <INSTITUTION id="I000002" sortcode="1" manager="Manager Name" name="Bank 0">
       <ADDRESS street="Somewhere" zip="1010" city="Wien" telephone="+431111"/>
       <ACCOUNTIDS/>       
      </INSTITUTION>
      <INSTITUTION id="I000003" sortcode="2" manager="Manager Name 2" name="Bank 1">
       <ADDRESS street="Somewhere 1" zip="1030" city="Linz" telephone="+43222"/>
       <ACCOUNTIDS/>
      </INSTITUTION>
  </INSTITUTIONS>
            """
        )
        val institutions = service.read(parser)
        assertEquals(2, institutions.size)

        val institution = institutions[1]
        assertEquals("I000003", institution.id)
        assertEquals("2", institution.sortCode)
        assertEquals("Manager Name 2", institution.manager)
        assertEquals("Bank 1", institution.name)

        assertEquals("Somewhere 1", institution.address.street)
        assertEquals("1030", institution.address.postCode)
        assertEquals("Linz", institution.address.city)
        assertEquals("+43222", institution.address.telephone)
    }

    @Test
    fun shouldBeAbleToReadEmptyInstitutions(){
        val parser = createParser(
            """
    <INSTITUTIONS count="0"/>
            """
        )
        val institutions = service.read(parser)
        assertEquals(0, institutions.size)
    }
}
