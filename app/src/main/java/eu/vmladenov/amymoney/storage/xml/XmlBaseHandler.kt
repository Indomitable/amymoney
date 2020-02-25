package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.KMyMoneyFile
import eu.vmladenov.amymoney.models.XmlAttribute
import eu.vmladenov.amymoney.models.XmlTag
import org.xmlpull.v1.XmlPullParser
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

interface IXmlFileTagHandler {
    fun update(parser: XmlPullParser, file: KMyMoneyFile)
}

abstract class XmlBaseHandler: IXmlFileTagHandler {

    // abstract override fun update(file: KMyMoneyFile)

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

    protected fun <TClass> getDateAttributeValue(parser: XmlPullParser, name: KProperty1<TClass, Date?>): Date? {
        val attribute = name.annotations.find { a -> a is XmlAttribute } as? XmlAttribute
        if (attribute != null) {
            val value = getAttributeValue(parser, attribute.value)
            return try { dateFormat.parse(value) } catch (e: ParseException) { null }
        }
        throw Exception("Property has no XmlAttribute annotation. Use name: String overload.")
    }

    protected fun <T> readChild(parser: XmlPullParser, childTag: XmlTags, handler: (parser: XmlPullParser) -> T): T {
        var child: T? = null
        val parentTag = XmlTags[parser.name]
        parseChildren(parser) {tagName, xmlParser ->
            if (tagName == childTag) {
                child = handler(xmlParser)
                return@parseChildren
            }
        }
        if (child == null) {
            throw XmlParseException(parentTag, "Child ${childTag.tagName} not found in ${parentTag.tagName}. Line: ${parser.lineNumber}")
        }
        return child!!
    }

    protected fun parseChildren(parser: XmlPullParser, handler: (tagName: XmlTags, parser: XmlPullParser) -> Unit) {
        val parentTag = XmlTags[parser.name]
        do {
            val eventType = parser.nextTag()
            val tagName = XmlTags[parser.name]
            if (eventType == XmlPullParser.START_TAG) {
                handler(tagName, parser)
            }
        } while (!(eventType == XmlPullParser.END_TAG && tagName == parentTag))
    }

    protected fun readIdList(parser: XmlPullParser, childTag: XmlTags): List<String> {
        val result = mutableListOf<String>()
        parseChildren(parser) { tagName, xmlParser ->
            if (tagName == childTag) {
                val id = getAttributeValue(xmlParser, "id")
                result.add(id)
            }
        }
        return result
    }

    protected fun readKeyValuePairs(parser: XmlPullParser): Map<String, String> {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.KeyValuePairs.tagName)
        val result = mutableMapOf<String, String>()
        parseChildren(parser) { pair, xmlParser ->
            if (pair == XmlTags.Pair) {
                val key = getAttributeValue(xmlParser, "key")
                val value = getAttributeValue(xmlParser, "value")
                result[key] = value
            }
        }
        return result
    }

    protected fun getXmlTag(type: KClass<*>): XmlTags {
        val tagAnnotation = type.annotations.find { a -> a is XmlTag } as? XmlTag
        if (tagAnnotation != null) {
            return tagAnnotation.value
        }
        throw Exception("Class ${type.simpleName} has no XmlTag annotation")
    }
}
