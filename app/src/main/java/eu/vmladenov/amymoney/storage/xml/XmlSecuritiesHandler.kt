package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.RoundingMethod
import eu.vmladenov.amymoney.models.Security
import eu.vmladenov.amymoney.models.SecurityType
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handling Securities and Currencies
 */
abstract class XmlBaseSecuritiesHandler(parentTag: XmlTags, childTag: XmlTags): XmlBaseModelCollectionHandler<Security>(parentTag, childTag) {
    override fun readChild(parser: XmlPullParser): Security {
        parser.require(XmlPullParser.START_TAG, null, childTag.tagName)
        checkUnsupportedAttributes(parser, Security::class)

        fun getSmallestAccountFraction(): Int {
            val saf = getAttributeValue(parser, Security::smallestAccountFraction)
            val defaultSmallestAccountFraction = 100
            return if (saf == "" || saf == "0") {
                defaultSmallestAccountFraction
            } else {
                try {
                    Integer.parseInt(saf)
                } catch (e: NumberFormatException) {
                    defaultSmallestAccountFraction
                }
            }
        }

        fun getPricePrecision(): Int {
            val pp = getAttributeValue(parser, Security::pricePrecision)
            val defaultPrecision = 4
            if (pp == "" || pp == "0") {
                return defaultPrecision
            } else {
                try {
                    val ppInt = Integer.parseInt(pp)
                    if (ppInt > 10) {
                        return defaultPrecision
                    }
                    return ppInt
                } catch (e: NumberFormatException) {
                    return defaultPrecision
                }
            }
        }

        val securityType = SecurityType.fromAttribute(getAttributeValue(parser, Security::securityType))

        fun getSmallestCashFraction(): Int {
            if (securityType != SecurityType.Currency) {
                return 0
            }
            val scf = getAttributeValue(parser, Security::smallestCashFraction)
            val defaultSmallestCashFraction = 100
            return if (scf == "" || scf == "0") {
                defaultSmallestCashFraction
            } else {
                try {
                    Integer.parseInt(scf)
                } catch (e: NumberFormatException) {
                    defaultSmallestCashFraction
                }
            }
        }

        fun getTradingCurrency(): String {
            return if (securityType == SecurityType.Currency) {
                ""
            } else {
                getAttributeValue(parser, Security::tradingCurrency)
            }
        }

        fun getTradingMarket(): String {
            return if (securityType == SecurityType.Currency) {
                ""
            } else {
                getAttributeValue(parser, Security::tradingMarket)
            }
        }

        val extraData = mutableMapOf<String, String>()
        val security = Security(
            id = getAttributeValue(parser, Security::id),
            name = getAttributeValue(parser, Security::name),
            tradingSymbol = getAttributeValue(parser, Security::tradingSymbol),
            securityType = securityType,
            roundingMethod = RoundingMethod.fromAttribute(getAttributeValue(parser, Security::roundingMethod)),
            smallestAccountFraction = getSmallestAccountFraction(),
            pricePrecision = getPricePrecision(),
            smallestCashFraction = getSmallestCashFraction(),
            tradingCurrency = getTradingCurrency(),
            tradingMarket = getTradingMarket(),
            extra = extraData
        )

        parseChildren(parser) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.KeyValuePairs ->
                    extraData.putAll(readKeyValuePairs(xmlParser))
                else ->
                    throw XmlParseException(tagName, "Unknown tag name ${tagName.tagName} found in ${childTag.tagName}. Line ${xmlParser.lineNumber}")
            }
        }
        return security
    }
}

@Singleton
class XmlCurrenciesHandler @Inject constructor(): XmlBaseSecuritiesHandler(XmlTags.Currencies, XmlTags.Currency) {
    override fun update(parser: XmlPullParser, file: XmlFile) {
        file.currencies.fill(readChildren(parser))
    }
}

@Singleton
class XmlSecuritiesHandler @Inject constructor(): XmlBaseSecuritiesHandler(XmlTags.Securities, XmlTags.Security) {
    override fun update(parser: XmlPullParser, file: XmlFile) {
        file.securities.fill(readChildren(parser))
    }
}
