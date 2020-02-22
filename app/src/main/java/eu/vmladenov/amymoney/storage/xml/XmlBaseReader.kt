package eu.vmladenov.amymoney.storage.xml

import org.xmlpull.v1.XmlPullParser
import java.text.SimpleDateFormat
import java.util.*

abstract class XmlBaseReader(protected val parser: XmlPullParser) {
    protected val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    protected fun getCurrentXmlTag(): XmlTags {
        return XmlTags[parser.name]
    }
}
