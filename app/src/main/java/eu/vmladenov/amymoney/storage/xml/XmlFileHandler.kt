package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.infrastructure.IProgressReporter
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlFileHandler {
    suspend fun read(parser: XmlPullParser, progressReporter: IProgressReporter): XmlFile
}

class XmlFileHandler @Inject constructor(
    val handlers: Map<XmlTags, @JvmSuppressWildcards IXmlFileTagHandler>
): IXmlFileHandler {

    private val kMyMoneyFileTagName = "KMYMONEY-FILE"

    override suspend fun read(parser: XmlPullParser, progressReporter: IProgressReporter): XmlFile {
        var eventType = parser.eventType
        val file = XmlFile()
        progressReporter.progress("Starting file processing...")
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == kMyMoneyFileTagName) {
                // we parse all top level tags of the file
                eventType = parser.nextTag() // We advance to next tag
                while (!(eventType == XmlPullParser.END_TAG && parser.name == kMyMoneyFileTagName)) {
                    if (eventType == XmlPullParser.START_TAG) {
                        val tag = XmlTags[parser.name]
                        progressReporter.progress("Reading ${parser.name} ...")
                        val handler = handlers[tag]
                            ?: throw XmlParseException(tag, "Not supported tag with name: ${tag.tagName} was found. Live: ${parser.lineNumber}")
                        handler.update(parser, file)
                    }
                    eventType = parser.nextTag()
                }
            }
            eventType = parser.next()
        }
        progressReporter.progress("Finish file processing!")
        return file
    }
}
