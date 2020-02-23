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

        val id: String = getAttributeValue(parser, Account::id)
        val name = getAttributeValue(parser, Account::name)
        val parentAccount = getAttributeValue(parser, Account::parentAccountId)
        val lastModified = getDateAttributeValue(parser, Account::lastModified)
        val lastReconciled = getDateAttributeValue(parser, Account::lastReconciliationDate)
        val institution = getAttributeValue(parser, Account::institutionId)
        val number = getAttributeValue(parser, Account::number)
        val openDate = getDateAttributeValue(parser, Account::openingDate)
        val currency = getAttributeValue(parser, Account::currencyId)
        val type = AccountType[Integer.parseInt(getAttributeValue(parser, Account::type))]
        val description = getAttributeValue(parser, Account::description)

        val subAccountIds = mutableListOf<String>()
        val extraData = mutableListOf<Pair<String, String>>()
        parseChildren(parser, XmlTags.Account) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.SubAccounts ->
                    parseChildren(xmlParser, tagName) { accountId, xmlParser1 ->
                        if (accountId == XmlTags.SubAccount) {
                            subAccountIds.add(getAttributeValue(xmlParser1, "id"))
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
                else -> throw XmlParseException(tagName, "Unkwnon tag name ${tagName.tagName} found in institution. Line ${xmlParser.lineNumber}")
            }
        }
        return Account(
            id,
            name,
            currency,
            type,
            lastReconciled,
            lastModified,
            parentAccount,
            openDate,
            institution,
            number,
            description,
            subAccountIds,
            extraData
        )
    }
}
