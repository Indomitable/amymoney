package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

interface IXmlFileHandler {
    fun read(parser: XmlPullParser): KMyMoneyState
}

@Singleton
class XmlFileHandler @Inject constructor(
    private val handlers: Map<XmlTags, @JvmSuppressWildcards IXmlFileTagHandler>
): IXmlFileHandler {

    override fun read(parser: XmlPullParser): KMyMoneyState {
        var eventType = parser.eventType
        val file = KMyMoneyFile()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                val tag = XmlTags[parser.name]
                handlers[tag]?.update(parser, file)
            }
            eventType = parser.next()
        }
        return file.state()
    }
}
