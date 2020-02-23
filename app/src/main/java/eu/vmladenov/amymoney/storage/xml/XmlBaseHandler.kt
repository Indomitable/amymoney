package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.XmlAttribute
import org.xmlpull.v1.XmlPullParser
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KProperty1

abstract class XmlBaseHandler {
    protected val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    protected fun getAttributeValue(parser: XmlPullParser, name: String): String {
        return parser.getAttributeValue(null, name) ?: ""
    }

    protected fun <TClass, TProp> getAttributeValue(parser: XmlPullParser, name: KProperty1<TClass, TProp>): String {
        val attribute = name.annotations.find { a -> a is XmlAttribute } as? XmlAttribute
        if (attribute != null) {
            return getAttributeValue(parser, attribute.value)
        }
        throw Exception("Property has no XmlAttribute annotation. Use name: String overload.")
    }

    protected fun <T> readChild(parser: XmlPullParser, parentTag: XmlTags, childTag: XmlTags, handler: (parser: XmlPullParser) -> T): T {
        var child: T? = null
        parseChildren(parser, parentTag) {tagName, xmlParser ->
            if (tagName == childTag) {
                child = handler(xmlParser)
                return@parseChildren
            }
        }
        if (child == null) {
            throw ParseException(parentTag, "Child ${childTag.tagName} not found in ${parentTag.tagName}. Line: ${parser.lineNumber}")
        }
        return child!!
    }

    protected fun parseChildren(parser: XmlPullParser, parentTag: XmlTags, handler: (tagName: XmlTags, parser: XmlPullParser) -> Unit) {
        do {
            val eventType = parser.nextTag()
            val tagName = XmlTags[parser.name]
            if (eventType == XmlPullParser.START_TAG) {
                handler(tagName, parser)
            }
        } while (!(eventType == XmlPullParser.END_TAG && tagName == parentTag))
    }
}
