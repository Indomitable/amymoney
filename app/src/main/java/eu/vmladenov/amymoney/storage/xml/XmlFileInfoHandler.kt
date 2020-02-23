package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.FileInfo
import org.xmlpull.v1.XmlPullParser
import java.util.*
import javax.inject.Inject

interface IXmlFileInfoHandler {
    fun read(parser: XmlPullParser): FileInfo
}

class XmlFileInfoHandler @Inject constructor() : XmlBaseHandler(), IXmlFileInfoHandler {
    override fun read(parser: XmlPullParser): FileInfo {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.FileInfo.tagName)

        var creationDate: Date? = null
        var lastModifyDate: Date? = null
        var version: Int? = null
        var fixVersion: Int? = null

        parseChildren(parser, XmlTags.FileInfo) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.CreationDate ->
                    creationDate = dateFormat.parse(getAttributeValue(xmlParser, "date"))
                XmlTags.LastModifiedDate ->
                    lastModifyDate = dateFormat.parse(getAttributeValue(xmlParser, "date"))
                XmlTags.Version ->
                    version = Integer.parseInt(getAttributeValue(xmlParser, "id"))
                XmlTags.FixVersion ->
                    fixVersion = Integer.parseInt(getAttributeValue(xmlParser, "id"))
                else ->
                    throw XmlParseException(tagName, "Unknown tag ${tagName.tagName} in File Info tag. Line: ${xmlParser.lineNumber}")
            }
        }
        return FileInfo(creationDate!!, lastModifyDate!!, version!!, fixVersion!!)
    }
}
