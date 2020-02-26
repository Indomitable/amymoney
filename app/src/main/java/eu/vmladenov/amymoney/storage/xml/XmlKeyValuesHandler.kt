package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.KMyMoneyFile
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlKeyValuesHandler @Inject constructor(): XmlBaseHandler(), IXmlFileTagHandler {
    override fun update(parser: XmlPullParser, file: KMyMoneyFile) {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.KeyValuePairs.tagName)
        for (pair in readPairs(parser)) {
            file.extra[pair.first] = pair.second
        }
    }

    private fun readPairs(parser: XmlPullParser) = iterChildren(parser) { _, xmlParser ->
        return@iterChildren Pair(
            getAttributeValue(xmlParser, "key"),
            getAttributeValue(xmlParser, "value")
        )
    }
}
