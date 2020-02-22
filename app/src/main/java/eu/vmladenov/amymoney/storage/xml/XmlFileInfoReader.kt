package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.FileInfo
import org.xmlpull.v1.XmlPullParser
import java.util.*
import javax.inject.Inject

interface IXmlFileInfoReader {
    fun read(): FileInfo
}

class XmlFileInfoReader @Inject constructor(parser: XmlPullParser): XmlBaseReader(parser), IXmlFileInfoReader {
    override fun read(): FileInfo {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.FileInfo.tagName)

        var creationDate: Date? = null
        var lastModifyDate: Date? = null
        var version: Int? = null
        var fixVersion: Int? = null

        do {
            val eventType = parser.nextTag()
            val tagName = getCurrentXmlTag()
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        XmlTags.CreationDate ->
                            creationDate = dateFormat.parse(parser.getAttributeValue(null, "date"))
                        XmlTags.LastModifiedDate ->
                            lastModifyDate = dateFormat.parse(parser.getAttributeValue(null, "date"))
                        XmlTags.Version ->
                            version = Integer.parseInt(parser.getAttributeValue(null, "id"))
                        XmlTags.FixVersion ->
                            fixVersion = Integer.parseInt(parser.getAttributeValue(null, "id"))
                        else ->
                            throw ParseException(tagName.tagName, "Unknown tag name in File Info")
                    }
                }
            }
        } while (!(eventType == XmlPullParser.END_TAG && tagName == XmlTags.FileInfo))
        return FileInfo(creationDate!!, lastModifyDate!!, version!!, fixVersion!!)
    }
}
