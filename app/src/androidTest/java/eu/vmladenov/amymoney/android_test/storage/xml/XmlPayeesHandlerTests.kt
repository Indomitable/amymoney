package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.infrastructure.AMyMoneyRepository
import eu.vmladenov.amymoney.models.IbanBicPayeeIdentifier
import eu.vmladenov.amymoney.models.NationalAccountPayeeIdentifier
import eu.vmladenov.amymoney.storage.xml.XmlParseException
import eu.vmladenov.amymoney.storage.xml.XmlPayeesHandler
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class XmlPayeesHandlerTests : BaseXmlHandlerTest() {
    private lateinit var service: XmlPayeesHandler

    @Before
    fun setup() {
        service = XmlPayeesHandler()
    }

    @Test
    //@DisplayName("Given proper user xml it should be able to read it properly")
    fun shouldBeAbleToRead() {
        val parser = createParser(
            """
 <PAYEES>                
  <PAYEE matchignorecase="1" matchingenabled="1" matchkey="Company 1&#xa;Company0" name="Company" usingmatchkey="1" id="P000004" reference="Reference" defaultaccountid="A000008" notes="Some hotes&#xa;More notes" email="company@server.com">
   <ADDRESS postcode="1203" city="Wien" street="Some address&#xa;sadd" telephone="+43111" state="Austria"/>
   <payeeIdentifier iban="AT12234564564666" type="org.kmymoney.payeeIdentifier.ibanbic" bic="AAAA"/>
   <payeeIdentifier bankcode="AT" type="org.kmymoney.payeeIdentifier.national" ownername="A Owner" country="A Country" accountnumber="2031231"/>
  </PAYEE>
 </PAYEES>
            """
        )

        val repo = AMyMoneyRepository()
        service.update(parser, repo)

        val payees = repo.payees
        Assert.assertEquals(1, payees.size)
        val payee = payees[0]
        Assert.assertEquals("P000004", payee.id)
        Assert.assertEquals("Company", payee.name)
        Assert.assertEquals("company@server.com", payee.email)
        Assert.assertEquals("Some hotes\nMore notes", payee.notes)
        Assert.assertEquals("Reference", payee.reference)
        Assert.assertEquals("A000008", payee.defaultAccountId)
        Assert.assertTrue(payee.isMatchingEnabled)
        Assert.assertTrue(payee.isUsingMatchKey)
        Assert.assertTrue(payee.isMatchKeyIgnoreCase)
        Assert.assertEquals("Company 1\nCompany0", payee.matchKey)

        val address = payee.address
        Assert.assertEquals("Austria", address.country)
        Assert.assertEquals("1203", address.postCode)
        Assert.assertEquals("Wien", address.city)
        Assert.assertEquals("Some address\nsadd", address.street)
        Assert.assertEquals("+43111", address.telephone)

        val identifiers = payee.identifiers
        Assert.assertEquals(2, identifiers.size)

        val iban = identifiers[0] as IbanBicPayeeIdentifier
        Assert.assertEquals("AT12234564564666", iban.iban)
        Assert.assertEquals("AAAA", iban.bic)

        val nationalAccount = identifiers[1] as NationalAccountPayeeIdentifier
        Assert.assertEquals("AT", nationalAccount.bankCode)
        Assert.assertEquals("2031231", nationalAccount.accountNumber)
        Assert.assertEquals("A Owner", nationalAccount.ownerName)
        Assert.assertEquals("A Country", nationalAccount.country)
    }

    @Test
    fun shouldBeAbleToReadWithNoIdentifiers() {
        val parser = createParser(
            """
 <PAYEES>                
  <PAYEE matchignorecase="1" matchingenabled="1" matchkey="Company 1&#xa;Company0" name="Company" usingmatchkey="1" id="P000004" reference="Reference" defaultaccountid="A000008" notes="Some hotes&#xa;More notes" email="company@server.com">
   <ADDRESS postcode="1203" city="Wien" street="Some address&#xa;sadd" telephone="+43111" state="Austria"/>
  </PAYEE>
 </PAYEES>
            """
        )
        val repo = AMyMoneyRepository()
        service.update(parser, repo)

        val payees = repo.payees
        Assert.assertEquals(1, payees.size)
        Assert.assertEquals(0, payees[0].identifiers.size)
    }

    @Test
    fun shouldBeAbleToReadMultiplePayees() {
        val parser = createParser(
            """
 <PAYEES>                
  <PAYEE matchignorecase="1" matchingenabled="1" matchkey="Company 1&#xa;Company0" name="Company" usingmatchkey="1" id="P000004" reference="Reference" defaultaccountid="A000008" notes="Some hotes&#xa;More notes" email="company@server.com">
   <ADDRESS postcode="1203" city="Wien" street="Some address&#xa;sadd" telephone="+43111" state="Austria"/>
  </PAYEE>
  <PAYEE matchignorecase="0" matchingenabled="0" matchkey="Company 1&#xa;Company0" name="Company" usingmatchkey="1" id="P000005" reference="Reference" defaultaccountid="A000008" notes="Some hotes&#xa;More notes" email="company@server.com">
   <ADDRESS postcode="" city="" street="" telephone="" state=""/>
  </PAYEE>
 </PAYEES>
            """
        )
        val repo = AMyMoneyRepository()
        service.update(parser, repo)

        val payees = repo.payees
        Assert.assertEquals(2, payees.size)
    }

    @Test
    fun shouldBeAbleToReadEmptyInstitutions() {
        val parser = createParser(
            """
    <PAYEES count="0"/>
            """
        )
        val repo = AMyMoneyRepository()
        service.update(parser, repo)

        val payees = repo.payees
        Assert.assertEquals(0, payees.size)
    }

    @Test(expected = XmlParseException::class)
    fun shouldThrowWhenContainsUnknownTag() {
        val parser = createParser(
            """
 <PAYEES>                
  <PAYEE matchignorecase="1" matchingenabled="1" matchkey="Company 1&#xa;Company0" name="Company" usingmatchkey="1" id="P000004" reference="Reference" defaultaccountid="A000008" notes="Some hotes&#xa;More notes" email="company@server.com">
    <ADDRESS postcode="" city="" street="" telephone="" state=""/>
    <UNKNOWN_TAG/>
  </PAYEE>
 </PAYEES>
            """
        )
        service.update(parser, AMyMoneyRepository())
    }
}
