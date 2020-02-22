package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Address
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.models.Institutions
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlInstitutionsHandler {
    fun read(parser: XmlPullParser): Institutions
}

class XmlInstitutionsHandler @Inject constructor() : XmlBaseHandler(), IXmlInstitutionsHandler {
    override fun read(parser: XmlPullParser): Institutions {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Institutions.tagName)
        val institutions = mutableListOf<Institution>()

        parseChildren(parser, XmlTags.Institutions) { tagName, xmlParser ->
            if (tagName == XmlTags.Institution) {
                institutions.add(readInstitution(xmlParser))
            }
        }

        return Institutions(institutions)
    }

    private fun readInstitution(parser: XmlPullParser): Institution {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Institution.tagName)

        val id = getAttributeValue(parser, "id")
        val sortCode = getAttributeValue(parser, "sortcode")
        val name = getAttributeValue(parser, "name")
        val manager = getAttributeValue(parser, "manager")

        var address: Address? = null
        var accountIds = mutableListOf<String>()
        var extraData = mutableListOf<Pair<String, String>>()
        parseChildren(parser, XmlTags.Institution) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.Address ->
                    address = Address(
                        city = getAttributeValue(xmlParser, "city"),
                        country = getAttributeValue(xmlParser, "county"),
                        postCode = getAttributeValue(xmlParser, "zip"),
                        street = getAttributeValue(xmlParser, "street"),
                        telephone = getAttributeValue(xmlParser, "telephone")
                    )
                XmlTags.AccountIds ->
                    parseChildren(xmlParser, tagName) { accountId, xmlParser1 ->
                        if (accountId == XmlTags.AccountId) {
                            accountIds.add(getAttributeValue(xmlParser1, "id"))
                        }
                    }
                XmlTags.KeyValuePairs ->
                    parseChildren(xmlParser, tagName) { pair, xmlParser2 ->
                        if (pair == XmlTags.Pair) {
                            extraData.add(
                                getAttributeValue(xmlParser2, "key") to getAttributeValue(xmlParser2, "value")
                            )
                        }
                    }
            }
        }
        if (address == null) {
            address = Address()
        }
        return Institution(id, name, sortCode, manager, address!!, accountIds, extraData)
    }
}
