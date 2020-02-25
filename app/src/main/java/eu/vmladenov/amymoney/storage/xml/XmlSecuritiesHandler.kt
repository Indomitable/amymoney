package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Securities
import eu.vmladenov.amymoney.models.Security
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlSecuritiesHandler {
    fun read(parser: XmlPullParser): Securities
}

/**
 * Handling Securities and Currencies
 */
open class XmlBaseSecuritiesHandler(parentTag: XmlTags, childTag: XmlTags): XmlBaseCollectionHandler<Security>(parentTag, childTag), IXmlSecuritiesHandler {
    override fun read(parser: XmlPullParser): Securities {
        return Securities(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Security {
        throw NotImplementedError()
    }
}

class XmlCurrenciesHandler @Inject constructor(): XmlBaseSecuritiesHandler(XmlTags.Currencies, XmlTags.Currency)
class XmlSecuritiesHandler @Inject constructor(): XmlBaseSecuritiesHandler(XmlTags.Securities, XmlTags.Security)
