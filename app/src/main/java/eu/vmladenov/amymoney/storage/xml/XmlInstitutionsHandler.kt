package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Address
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.models.Institutions
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlInstitutionsHandler {
    fun read(parser: XmlPullParser): Institutions
}

class XmlInstitutionsHandler @Inject constructor() : XmlBaseCollectionHandler<Institution>(XmlTags.Institutions, XmlTags.Institution), IXmlInstitutionsHandler {
    override fun read(parser: XmlPullParser): Institutions {
        return Institutions(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Institution {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Institution.tagName)

        val accountIds = mutableListOf<String>()
        val extraData = mutableMapOf<String, String>()
        val institution = Institution(
            id = getAttributeValue(parser, Institution::id),
            sortCode = getAttributeValue(parser, Institution::sortCode),
            name = getAttributeValue(parser, Institution::name),
            manager = getAttributeValue(parser, Institution::manager),
            accountIds = accountIds,
            extra = extraData,
            address = Address()
        )
        parseChildren(parser, XmlTags.Institution) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.Address -> {
                    institution.address.city = getAttributeValue(xmlParser, "city")
                    institution.address.country = getAttributeValue(xmlParser, "county")
                    institution.address.postCode = getAttributeValue(xmlParser, "zip")
                    institution.address.street = getAttributeValue(xmlParser, "street")
                    institution.address.telephone = getAttributeValue(xmlParser, "telephone")
                }
                XmlTags.AccountIds ->
                    accountIds.addAll(readIdList(xmlParser, XmlTags.AccountIds, XmlTags.AccountId))
                XmlTags.KeyValuePairs ->
                    extraData.putAll(readKeyValuePairs(xmlParser))
                else -> throw XmlParseException(tagName, "Unkwnon tag name ${tagName.tagName} found in institution. Line ${xmlParser.lineNumber}")
            }
        }
        return institution
    }
}
