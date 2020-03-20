package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.models.Price
import eu.vmladenov.amymoney.models.PricePair
import org.xmlpull.v1.XmlPullParser
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlPricesHandler @Inject constructor(): XmlBaseCollectionHandler<PricePair>(XmlTags.Prices, XmlTags.PricePair) {
    private val defaultPriceDate: Date = GregorianCalendar(1998, 11, 31).time

    override fun update(parser: XmlPullParser, file: XmlFile) {
        file.prices.fill(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): PricePair {
        return PricePair (
            from = getAttributeValue(parser, PricePair::from),
            to = getAttributeValue(parser, PricePair::to),
            prices = iterChildren(parser) { _, priceParser ->
                checkUnsupportedAttributes(parser, Price::class)
                return@iterChildren Price(
                    source = getAttributeValue(priceParser, Price::source),
                    price = Fraction.parseFraction(getAttributeValue(priceParser, Price::price)),
                    date = getDateAttributeValue(priceParser, Price::date) ?: defaultPriceDate
                )
            }.toList()
        )
    }
}
