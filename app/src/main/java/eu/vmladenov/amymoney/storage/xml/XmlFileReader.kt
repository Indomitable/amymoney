package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.*
import eu.vmladenov.amymoney.Payee
import org.xmlpull.v1.XmlPullParser
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ParseException(val tagName: String, message: String) : Exception(message)

interface IXmlFileReader {
    fun read(): KMyMoneyFile
}

class XmlFileReader @Inject constructor (
    parser: XmlPullParser,
    private val fileInfoReader: IXmlFileInfoReader,
    private val userReader: IXmlUserReader
): XmlBaseReader(parser), IXmlFileReader {

    override fun read(): KMyMoneyFile {
        var eventType = parser.eventType
        var fileInfo: FileInfo? = null
        var user: User? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (getCurrentXmlTag()) {
                        XmlTags.FileInfo -> fileInfo = fileInfoReader.read()
                        XmlTags.User -> user = userReader.read()
                    }
                }
            }
            eventType = parser.next()
        }
        return KMyMoneyFile(fileInfo!!, user!!)
    }

/*    private fun readPayee(): Payee {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Payee.tagName)

        val id: String = parser.getAttributeValue(null, "id") ?: ""
        val name: String = parser.getAttributeValue(null, "name")
        val email: String = parser.getAttributeValue(null, "email") ?: ""
        val notes: String = parser.getAttributeValue(null, "notes") ?: ""
        val reference: String = parser.getAttributeValue(null, "reference") ?: ""
        val defaultAccountId: String = parser.getAttributeValue(null, "defaultaccountid") ?: ""
        val isMatchingEnabled: Boolean = parser.getAttributeValue(null, "matchingenabled") == "1"
        val isUsingMatchKey: Boolean = parser.getAttributeValue(null, "usingmatchkey") == "1"
        val isMatchKeyIgnoreCase: Boolean = parser.getAttributeValue(null, "matchignorecase") == "1"
        val matchKey: String = parser.getAttributeValue(null, "matchkey") ?: ""

        parser.nextTag()
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Address.tagName)
        val address = Address(
            city = parser.getAttributeValue(null, "city") ?: "",
            country = parser.getAttributeValue(null, "state") ?: "",
            postCode = parser.getAttributeValue(null, "postcode") ?: "",
            street = parser.getAttributeValue(null, "street") ?: "",
            telephone = parser.getAttributeValue(null, "telephone") ?: ""
        )

        return Payee(id, name, email, notes, reference, defaultAccountId, isMatchingEnabled, isUsingMatchKey, isMatchKeyIgnoreCase, matchKey, address)
    }*/
}