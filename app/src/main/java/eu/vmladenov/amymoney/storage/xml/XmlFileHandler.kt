package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.FileInfo
import eu.vmladenov.amymoney.models.Institutions
import eu.vmladenov.amymoney.models.KMyMoneyState
import eu.vmladenov.amymoney.models.User
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlFileHandler {
    fun read(parser: XmlPullParser): KMyMoneyState
}

class XmlFileHandler @Inject constructor (
    private val fileInfoHandler: IXmlFileInfoHandler,
    private val userHandler: IXmlUserHandler,
    private val institutionsHandler: IXmlInstitutionsHandler
): XmlBaseHandler(), IXmlFileHandler {

    override fun read(parser: XmlPullParser): KMyMoneyState {
        var eventType = parser.eventType
        var fileInfo: FileInfo? = null
        var user: User? = null
        var institutions: Institutions? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (XmlTags[parser.name]) {
                        XmlTags.FileInfo -> fileInfo = fileInfoHandler.read(parser)
                        XmlTags.User -> user = userHandler.read(parser)
                        XmlTags.Institutions -> institutions = institutionsHandler.read(parser)
                    }
                }
            }
            eventType = parser.next()
        }
        return KMyMoneyState(fileInfo!!, user!!, institutions!!)
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