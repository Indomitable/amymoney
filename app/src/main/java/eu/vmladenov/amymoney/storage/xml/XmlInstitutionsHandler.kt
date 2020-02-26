package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Address
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.models.Institutions
import eu.vmladenov.amymoney.models.KMyMoneyFile
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

interface IXmlInstitutionsHandler {
    fun read(parser: XmlPullParser): Institutions
}

@Singleton
class XmlInstitutionsHandler @Inject constructor() : XmlBaseCollectionHandler<Institution>(XmlTags.Institutions, XmlTags.Institution), IXmlInstitutionsHandler {
    override fun update(parser: XmlPullParser, file: KMyMoneyFile) {
        file.institutions = read(parser)
    }

    override fun read(parser: XmlPullParser): Institutions {
        return Institutions(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Institution {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Institution.tagName)
        checkUnsupportedAttributes(parser, Institution::class)

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
        parseChildren(parser) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.Address -> {
                    institution.address.city = getAttributeValue(xmlParser, "city")
                    institution.address.country = getAttributeValue(xmlParser, "county")
                    institution.address.postCode = getAttributeValue(xmlParser, "zip")
                    institution.address.street = getAttributeValue(xmlParser, "street")
                    institution.address.telephone = getAttributeValue(xmlParser, "telephone")
                }
                XmlTags.AccountIds ->
                    accountIds.addAll(readIdList(xmlParser, XmlTags.AccountId))
                XmlTags.KeyValuePairs ->
                    extraData.putAll(readKeyValuePairs(xmlParser))
                else -> throw XmlParseException(tagName, "Unknown tag name ${tagName.tagName} found in institution. Line ${xmlParser.lineNumber}")
            }
        }
        return institution
    }
}
