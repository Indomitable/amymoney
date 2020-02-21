package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.Address
import eu.vmladenov.amymoney.FileInfo
import eu.vmladenov.amymoney.KMyMoneyFile
import eu.vmladenov.amymoney.Payee
import org.xmlpull.v1.XmlPullParser
import java.text.SimpleDateFormat
import java.util.*

class XmlFileReader(val parser: XmlPullParser) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)


    fun read(): KMyMoneyFile {
        var eventType = parser.eventType
        var fileInfo: FileInfo? = null
        var user: Payee? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (getCurrentXmlTag()) {
                        XmlTags.FileInfo -> fileInfo = readFileInfo()
                        XmlTags.User -> user = readPayee()
                    }
                }
            }
            eventType = parser.next()
        }
        return KMyMoneyFile(fileInfo!!, user!!)
    }

    private fun getCurrentXmlTag(): XmlTags {
        return XmlTags[parser.name]
    }

    private fun readFileInfo(): FileInfo {
        var eventType = parser.eventType
        var tagName = getCurrentXmlTag()

        var creationDate: Date? = null
        var lastModifyDate: Date? = null
        var version: Int? = null
        var fixVersion: Int? = null

        do {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        XmlTags.CreationDate -> creationDate =
                            dateFormat.parse(parser.getAttributeValue(null, "date"))
                        XmlTags.LastModifiedDate -> lastModifyDate =
                            dateFormat.parse(parser.getAttributeValue(null, "date"))
                        XmlTags.Version -> version =
                            Integer.parseInt(parser.getAttributeValue(null, "id"))
                        XmlTags.FixVersion -> fixVersion =
                            Integer.parseInt(parser.getAttributeValue(null, "id"))
                    }
                }
            }
            eventType = parser.nextTag()
            tagName = getCurrentXmlTag()
        } while (!(eventType == XmlPullParser.END_TAG && tagName == XmlTags.FileInfo))
        return FileInfo(creationDate!!, lastModifyDate!!, version!!, fixVersion!!)
    }

    private fun readPayee(): Payee {
        var eventType = parser.eventType
        val startTagName = getCurrentXmlTag()

        if (eventType != XmlPullParser.START_TAG && !(startTagName == XmlTags.User || startTagName == XmlTags.Payee)) {
            throw Exception("Expected start tag User or Payee")
        }

        val id: String = parser.getAttributeValue(null, "id") ?: ""
        val name: String = parser.getAttributeValue(null, "name")
        val email: String = parser.getAttributeValue(null, "email") ?: ""
        val notes: String = parser.getAttributeValue(null, "notes") ?: ""
        val reference: String = parser.getAttributeValue(null, "reference") ?: ""
        val isMatchingEnabled: Boolean = parser.getAttributeValue(null, "matchingenabled") == "1"
        val isUsingMatchKey: Boolean = parser.getAttributeValue(null, "usingmatchkey") == "1"
        val isMatchKeyIgnoreCase: Boolean = parser.getAttributeValue(null, "matchignorecase") == "1"
        val matchKey: String = parser.getAttributeValue(null, "matchkey") ?: ""

        var address: Address? = null
        eventType = parser.nextTag()
        if (eventType == XmlPullParser.START_TAG && getCurrentXmlTag() == XmlTags.Address) {
            address = Address(
                city = parser.getAttributeValue(null, "city") ?: "",
                country = parser.getAttributeValue(null, "state") ?: "",
                postCode = parser.getAttributeValue(null, "postcode") ?: "",
                street = parser.getAttributeValue(null, "street") ?: "",
                telephone = parser.getAttributeValue(null, "telephone") ?: ""
            )
        }

        do {
            // iterate until end of Payee/User tag.
            eventType = parser.nextTag()
            val tagName = getCurrentXmlTag()
        }
        while (!(eventType == XmlPullParser.END_TAG && tagName == startTagName))

        if (address != null) {
            return Payee(id, name, email, notes, reference, isMatchingEnabled, isUsingMatchKey, isMatchKeyIgnoreCase, matchKey, address)
        }
        return Payee(id, name, email, notes, reference, isMatchingEnabled, isUsingMatchKey, isMatchKeyIgnoreCase, matchKey)

    }
}