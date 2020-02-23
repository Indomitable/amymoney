package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.models.AccountType
import eu.vmladenov.amymoney.storage.xml.XmlAccountHandler
import org.junit.Assert
import org.junit.Test
import java.util.*

class XmlAccountHandlerTests : BaseXmlHandlerTest() {
    lateinit var service: XmlAccountHandler

    @Test
    fun shouldParseAccount() {
        val parser = createParser(
            """<ACCOUNTS>
    <ACCOUNT name="Test Account" currency="EUR" description="Some Test Account"
        id="A000001" institution="I000001" lastmodified="2020-02-23" lastreconciled="2020-02-01" number="1010"
        opened="2020-01-01" parentaccount="AStd::Asset" type="1">
        <SUBACCOUNTS>
            <SUBACCOUNT id="A000132" />
            <SUBACCOUNT id="A000133" />
        </SUBACCOUNTS>
        <KEYVALUEPAIRS>
            <PAIR key="PreferredAccount" value="Yes"/>
            <PAIR key="iban" value="AT34234234234234"/>
            <PAIR key="lastNumberUsed" value="1"/>
            <PAIR key="maxCreditEarly" value="-10000/1"/>
            <PAIR key="minBalanceEarly" value="100/1"/>
        </KEYVALUEPAIRS>
    </ACCOUNT>
</ACCOUNTS>"""
        )
        service = XmlAccountHandler()
        val accounts = service.read(parser)

        Assert.assertEquals(1, accounts.size)
        val account = accounts[0]
        Assert.assertEquals("A000001", account.id)
        Assert.assertEquals("Test Account", account.name)
        Assert.assertEquals("EUR", account.currencyId)
        Assert.assertEquals(AccountType.Checkings, account.type)
        Assert.assertEquals(GregorianCalendar(2020, 1, 1).time, account.lastReconciliationDate)
        Assert.assertEquals(GregorianCalendar(2020, 1, 23).time, account.lastModified)
        Assert.assertEquals("AStd::Asset", account.parentAccountId)
        Assert.assertEquals(GregorianCalendar(2020, 0, 1).time, account.openingDate)
        Assert.assertEquals("I000001", account.institutionId)
        Assert.assertEquals("1010", account.number)
        Assert.assertEquals("Some Test Account", account.description)

        Assert.assertEquals(2, account.subAccounts.size)
        Assert.assertEquals("A000132", account.subAccounts[0])
        Assert.assertEquals("A000133", account.subAccounts[1])


        Assert.assertEquals(5, account.extra.size)
        Assert.assertEquals(Pair("PreferredAccount", "Yes"), account.extra[0])
        Assert.assertEquals(Pair("iban", "AT34234234234234"), account.extra[1])
        Assert.assertEquals(Pair("lastNumberUsed", "1"), account.extra[2])
        Assert.assertEquals(Pair("maxCreditEarly", "-10000/1"), account.extra[3])
        Assert.assertEquals(Pair("minBalanceEarly", "100/1"), account.extra[4])
    }

    @Test
    fun shouldParseMultipleAccounts() {
        val parser = createParser(
            """<ACCOUNTS>
  <ACCOUNT description="" lastmodified="" name="Lottery or Premium Bond Prizes" number="" type="12" parentaccount="A000018" lastreconciled="" id="A000023" opened="1900-01-01" institution="" currency="USD"/>
  <ACCOUNT description="" lastmodified="" name="Electricity" number="" type="13" parentaccount="A000033" lastreconciled="" id="A000034" opened="1900-01-01" institution="" currency="EUR"/>
</ACCOUNTS>"""
        )
        service = XmlAccountHandler()
        val accounts = service.read(parser)
        Assert.assertEquals(2, accounts.size)
        val account0 = accounts[0]
        Assert.assertEquals("A000023", account0.id)
        Assert.assertEquals("Lottery or Premium Bond Prizes", account0.name)
        Assert.assertEquals("USD", account0.currencyId)
        Assert.assertEquals(AccountType.Income, account0.type)
        Assert.assertNull(account0.lastReconciliationDate)
        Assert.assertNull(account0.lastModified)
        Assert.assertEquals("A000018", account0.parentAccountId)
        Assert.assertEquals(GregorianCalendar(1900, 0, 1).time, account0.openingDate)
        Assert.assertEquals("", account0.institutionId)
        Assert.assertEquals("", account0.number)
        Assert.assertEquals("", account0.description)

        val account1 = accounts[1]
        Assert.assertEquals("A000034", account1.id)
        Assert.assertEquals("Electricity", account1.name)
        Assert.assertEquals("EUR", account1.currencyId)
        Assert.assertEquals(AccountType.Expense, account1.type)
        Assert.assertNull(account1.lastReconciliationDate)
        Assert.assertNull(account1.lastModified)
        Assert.assertEquals("A000033", account1.parentAccountId)
        Assert.assertEquals(GregorianCalendar(1900, 0, 1).time, account1.openingDate)
        Assert.assertEquals("", account1.institutionId)
        Assert.assertEquals("", account1.number)
        Assert.assertEquals("", account1.description)
    }
}
