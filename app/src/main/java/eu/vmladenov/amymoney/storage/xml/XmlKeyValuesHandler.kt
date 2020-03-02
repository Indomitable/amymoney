package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlKeyValuesHandler @Inject constructor(): XmlBaseHandler(), IXmlFileTagHandler {
    override fun update(parser: XmlPullParser, repository: IAMyMoneyRepository) {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.KeyValuePairs.tagName)
        val map = mutableMapOf<String, String>()
        for (pair in readPairs(parser)) {
            map[pair.first] = pair.second
        }
        repository.extra = map
    }

    private fun readPairs(parser: XmlPullParser) = iterChildren(parser) { _, xmlParser ->
        return@iterChildren Pair(
            getAttributeValue(xmlParser, "key"),
            getAttributeValue(xmlParser, "value")
        )
    }
}
