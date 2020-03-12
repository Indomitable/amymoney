package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.UnsupportedTag
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In order to preserve the xml file the same way it was we need to save all data which is not suported,
 * and when write back the xml file we will write it again there.
 */
@Singleton
class XmlUnsupportedHandler @Inject constructor(): IXmlFileTagHandler {
    override fun update(parser: XmlPullParser, file: XmlFile) {
        file.unsupportedTags.add(read(parser))
    }

    private fun read(parser: XmlPullParser): UnsupportedTag {
        return UnsupportedTag(
            parser.name,
            attributes = readAttributes(parser),
            children = readChildren(parser, parser.name)
        )
    }

    private fun readAttributes(parser: XmlPullParser): Map<String, String> {
        val attributes = mutableMapOf<String, String>()
        for (i in 0 until parser.attributeCount) {
            attributes[parser.getAttributeName(i)] = parser.getAttributeValue(i)
        }
        return attributes
    }

    private fun readChildren(parser: XmlPullParser, parentName: String): List<UnsupportedTag> {
        parser.require(XmlPullParser.START_TAG, null, parentName)
        val result = mutableListOf<UnsupportedTag>()
        var eventType = parser.nextTag()
        while (!(eventType == XmlPullParser.END_TAG && parser.name == parentName)) {
            result.add(read(parser))
            eventType = parser.nextTag()
        }
        return result
    }
}
