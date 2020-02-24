package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.AccountType
import eu.vmladenov.amymoney.models.Accounts
import org.xmlpull.v1.XmlPullParser

interface IXmlAccountHandler {
    fun read(parser: XmlPullParser): Accounts
}

class XmlAccountHandler: XmlBaseCollectionHandler<Account>(XmlTags.Accounts, XmlTags.Account), IXmlAccountHandler {
    override fun read(parser: XmlPullParser): Accounts {
        return Accounts(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Account {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Account.tagName)

        val subAccountIds = mutableListOf<String>()
        val extraData = mutableMapOf<String, String>()

        val account = Account(
            id = getAttributeValue(parser, Account::id),
            name = getAttributeValue(parser, Account::name),
            currencyId = getAttributeValue(parser, Account::currencyId),
            type = AccountType[Integer.parseInt(getAttributeValue(parser, Account::type))],
            parentAccountId = getAttributeValue(parser, Account::parentAccountId),
            lastModified = getDateAttributeValue(parser, Account::lastModified),
            lastReconciliationDate = getDateAttributeValue(parser, Account::lastReconciliationDate),
            institutionId = getAttributeValue(parser, Account::institutionId),
            number = getAttributeValue(parser, Account::number),
            openingDate = getDateAttributeValue(parser, Account::openingDate),
            description = getAttributeValue(parser, Account::description),
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
