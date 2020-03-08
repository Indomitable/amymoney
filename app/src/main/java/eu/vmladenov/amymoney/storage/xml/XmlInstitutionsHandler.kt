package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Address
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.models.Institutions
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class XmlInstitutionsHandler @Inject constructor() : XmlBaseModelCollectionHandler<Institution>(XmlTags.Institutions, XmlTags.Institution) {
    override fun update(parser: XmlPullParser, repository: IAMyMoneyRepository) {
        val institutions = Institutions()
        institutions.fill(readChildren(parser))
        repository.institutions.onNext(institutions)
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
