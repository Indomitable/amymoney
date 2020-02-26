package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.storage.xml.XmlPricesHandler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class XmlPricesHandlerTests: BaseXmlHandlerTest() {
    private lateinit var service: XmlPricesHandler
    private val defaultPriceDate: Date = GregorianCalendar(1998, 11, 31).time

    @Before
    fun setup() {
        service = XmlPricesHandler()
    }

    @Test
    fun shouldBeAbleToParsePrices() {
        val parser = createParser("""<PRICES count="3">
    <PRICEPAIR from="ATS" to="EUR">
        <PRICE date="1998-12-31" price="10000/137603" source="KMyMoney" />
        <PRICE date="" price="10000/137603" source="KMyMoney" />
    </PRICEPAIR>
    <PRICEPAIR from="AZM" to="AZN">
        <PRICE date="2020-02-21" price="1/5000" source="KMyMoney" />
    </PRICEPAIR>
    <PRICEPAIR from="BEF" to="EUR">
        <PRICE date="1998-12-31" price="10000/403399" source="KMyMoney" />
    </PRICEPAIR>
</PRICES>
        """)
        val prices = service.read(parser)
        Assert.assertEquals(3, prices.size)
        val pricePair0 = prices[0]
        Assert.assertEquals("ATS", pricePair0.from)
        Assert.assertEquals("EUR", pricePair0.to)
        Assert.assertEquals(2, pricePair0.prices.size)
        Assert.assertEquals("10000/137603", pricePair0.prices[0].price)
        Assert.assertEquals("KMyMoney", pricePair0.prices[0].source)
        Assert.assertEquals(defaultPriceDate, pricePair0.prices[0].date)

        Assert.assertEquals(defaultPriceDate, pricePair0.prices[1].date)

        Assert.assertEquals("1/5000", prices[1].prices[0].price)
        Assert.assertEquals(GregorianCalendar(2020, 1, 21).time, prices[1].prices[0].date)
    }

    @Test
    fun shouldSetDateToDefaultOneWhenMissingOrWrong() {
        val parser = createParser("""<PRICES count="3">
    <PRICEPAIR from="ATS" to="EUR">
        <PRICE date="" price="10000/137603" source="KMyMoney" />
        <PRICE date="wrong-date" price="10000/137603" source="KMyMoney" />
    </PRICEPAIR>
</PRICES>
    """)
        val prices = service.read(parser)
        Assert.assertEquals(defaultPriceDate, prices.pricePairs[0].prices[0].date)
        Assert.assertEquals(defaultPriceDate, prices.pricePairs[0].prices[1].date)
    }
}
