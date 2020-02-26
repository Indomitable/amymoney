package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.models.RoundingMethod
import eu.vmladenov.amymoney.models.SecurityType
import eu.vmladenov.amymoney.storage.xml.XmlCurrenciesHandler
import eu.vmladenov.amymoney.storage.xml.XmlSecuritiesHandler
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class XmlSecuritiesHandlerTests: BaseXmlHandlerTest() {
    private lateinit var securitiesHandler: XmlSecuritiesHandler
    private lateinit var currenciesHandler: XmlCurrenciesHandler

    @Before
    fun setup() {
        securitiesHandler = XmlSecuritiesHandler()
        currenciesHandler = XmlCurrenciesHandler()
    }

    @Test
    fun shouldParseCurrencies() {
        val parser = createParser("""<CURRENCIES count="28">
    <CURRENCY name="Austrian Schilling" id="ATS" pp="4" rounding-method="0" saf="100" scf="100" symbol="ÖS" type="3" />
    <CURRENCY name="Azerbaijani Manat" id="AZM" pp="4" rounding-method="0" saf="100" scf="100" symbol="m." type="3" />
    <CURRENCY name="Belgian Franc" id="BEF" pp="4" rounding-method="0" saf="100" scf="100" symbol="Fr" type="3" />
    <CURRENCY name="Bulgarian Lev" id="BGL" pp="4" rounding-method="0" saf="100" scf="100" symbol="BGL" type="3" />
    <CURRENCY name="Bulgarian Lev (new)" id="BGN" pp="4" rounding-method="0" saf="100" scf="100" symbol="BGN" type="3" />
</CURRENCIES>
        """)
        val currencies = currenciesHandler.read(parser)
        Assert.assertEquals(5, currencies.size)

        val currency0 = currencies[0]
        Assert.assertEquals("Austrian Schilling", currency0.name)
        Assert.assertEquals("ATS", currency0.id)
        Assert.assertEquals(4, currency0.pricePrecision)
        Assert.assertEquals(RoundingMethod.RoundNever, currency0.roundingMethod)
        Assert.assertEquals(100, currency0.smallestAccountFraction)
        Assert.assertEquals(100, currency0.smallestCashFraction)
        Assert.assertEquals("ÖS", currency0.tradingSymbol)
        Assert.assertEquals(SecurityType.Currency, currency0.securityType)
    }

    @Test
    fun shouldSetSmallestAccountFractionTo100WhenNone() {
        val parser = createParser("""<CURRENCIES count="28">
    <CURRENCY name="Bulgarian Lev" id="BGN" pp="4" rounding-method="0" saf="" scf="100" symbol="BGN" type="3" />
    <CURRENCY type="3" id="EUR" symbol="€" pp="4" scf="100" saf="0" name="Euro" rounding-method="0"/>
    <CURRENCY type="3" id="RUB" symbol="RUB" pp="4" scf="100" saf="abc" name="Russian Ruble" rounding-method="0"/>
</CURRENCIES>
        """)
        val currencies = currenciesHandler.read(parser)
        Assert.assertEquals(100, currencies[0].smallestAccountFraction)
        Assert.assertEquals(100, currencies[1].smallestAccountFraction)
        Assert.assertEquals(100, currencies[2].smallestAccountFraction)
    }

    @Test
    fun shouldSetSmallestCashFractionTo100WhenNone() {
        val parser = createParser("""<CURRENCIES count="28">
    <CURRENCY name="Bulgarian Lev" id="BGN" pp="4" rounding-method="0" saf="50" scf="" symbol="BGN" type="3" />
    <CURRENCY type="3" id="EUR" symbol="€" pp="4" scf="0" saf="50" name="Euro" rounding-method="0"/>
    <CURRENCY type="3" id="RUB" symbol="RUB" pp="4" scf="abc" saf="50" name="Russian Ruble" rounding-method="0"/>
</CURRENCIES>
        """)
        val currencies = currenciesHandler.read(parser)
        Assert.assertEquals(100, currencies[0].smallestCashFraction)
        Assert.assertEquals(100, currencies[1].smallestCashFraction)
        Assert.assertEquals(100, currencies[2].smallestCashFraction)
    }

    @Test
    fun shouldSetPricePrecisionTo4WhenNone() {
        val parser = createParser("""<CURRENCIES count="28">
    <CURRENCY name="Bulgarian Lev" id="BGN" pp="" rounding-method="0" saf="50" scf="50" symbol="BGN" type="3" />
    <CURRENCY type="3" id="EUR" symbol="€" pp="0" scf="50" saf="50" name="Euro" rounding-method="0"/>
    <CURRENCY type="3" id="RUB" symbol="RUB" pp="abc" scf="50" saf="50" name="Russian Ruble" rounding-method="0"/>
</CURRENCIES>
        """)
        val currencies = currenciesHandler.read(parser)
        Assert.assertEquals(4, currencies[0].pricePrecision)
        Assert.assertEquals(4, currencies[1].pricePrecision)
        Assert.assertEquals(4, currencies[2].pricePrecision)
    }
}
