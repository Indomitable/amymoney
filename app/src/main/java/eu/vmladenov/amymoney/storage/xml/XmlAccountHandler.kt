package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.AccountType
import eu.vmladenov.amymoney.models.XmlAccount
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlAccountHandler @Inject constructor(): XmlBaseModelCollectionHandler<XmlAccount>(XmlTags.Accounts, XmlTags.Account) {
    override fun update(parser: XmlPullParser, file: XmlFile) {
        file.accounts = readChildrenMap(parser)
    }

    override fun readChild(parser: XmlPullParser): XmlAccount {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Account.tagName)
        checkUnsupportedAttributes(parser, XmlAccount::class)

        val subAccountIds = mutableListOf<String>()
        val extraData = mutableMapOf<String, String>()

        val account = XmlAccount(
            id = getAttributeValue(parser, XmlAccount::id),
            name = getAttributeValue(parser, XmlAccount::name),
            currencyId = getAttributeValue(parser, XmlAccount::currencyId),
            type = AccountType[Integer.parseInt(getAttributeValue(parser, XmlAccount::type))],
            parentAccountId = getAttributeValue(parser, XmlAccount::parentAccountId),
            lastModified = getDateAttributeValue(parser, XmlAccount::lastModified),
            lastReconciliationDate = getDateAttributeValue(parser, XmlAccount::lastReconciliationDate),
            institutionId = getAttributeValue(parser, XmlAccount::institutionId),
            number = getAttributeValue(parser, XmlAccount::number),
            openingDate = getDateAttributeValue(parser, XmlAccount::openingDate),
            description = getAttributeValue(parser, XmlAccount::description),
            subAccounts = subAccountIds,
            extra = extraData
        )

        parseChildren(parser) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.SubAccounts ->
                    subAccountIds.addAll(readIdList(xmlParser, XmlTags.SubAccount))
                XmlTags.KeyValuePairs ->
                    extraData.putAll(readKeyValuePairs(xmlParser))
                else ->
                    throw XmlParseException(tagName, "Unknown tag name ${tagName.tagName} found in institution. Line ${xmlParser.lineNumber}")
            }
        }
        return account
    }

}
