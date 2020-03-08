package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlTransactionsHandler @Inject constructor(): XmlBaseModelCollectionHandler<Transaction>(XmlTags.Transactions, XmlTags.Transaction) {
    override fun update(parser: XmlPullParser, repository: IAMyMoneyRepository) {
        repository.transactions.fill(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Transaction {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Transaction.tagName)
        checkUnsupportedAttributes(parser, Transaction::class)

        val extraData = mutableMapOf<String, String>()
        val transaction = Transaction(
            id = getAttributeValue(parser, Transaction::id),
            entryDate = getDateAttributeValue(parser, Transaction::entryDate),
            postDate = getDateAttributeValue(parser, Transaction::postDate),
            bankId = getAttributeValue(parser, Transaction::bankId),
            memo = getAttributeValue(parser, Transaction::memo),
            commodity = getAttributeValue(parser, Transaction::commodity),
            splits = Splits(),
            extra = extraData
        )

        parseChildren(parser) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.Splits ->
                    parseChildren(parser) { split, splitsParser ->
                        if (split == XmlTags.Split) {
                            transaction.splits.add(readSplit(splitsParser))
                        }
                    }
                XmlTags.KeyValuePairs ->
                    extraData.putAll(readKeyValuePairs(xmlParser))
                else ->
                    throw XmlParseException(tagName, "Unknown tag name ${tagName.tagName} found in transaction. Line ${xmlParser.lineNumber}")
            }
        }

        return transaction
    }

    private fun readSplit(parser: XmlPullParser): Split {
        checkUnsupportedAttributes(parser, Split::class)

        val tagIds = mutableListOf<String>()
        val extraData = mutableMapOf<String, String>()
        val split = Split(
            id = getAttributeValue(parser, Split::id),
            shares = Fraction.parseFraction(getAttributeValue(parser, Split::shares)),
            price = Fraction.parseFraction(getAttributeValue(parser, Split::price)),
            value = Fraction.parseFraction(getAttributeValue(parser, Split::value)),
            accountId = getAttributeValue(parser, Split::accountId),
            constCenterId = getAttributeValue(parser, Split::constCenterId),
            reconcileFlag = ReconciledState.fromAttribute(getAttributeValue(parser, Split::reconcileFlag)),
            reconcileDate = getDateAttributeValue(parser, Split::reconcileDate),
            payeeId = getAttributeValue(parser, Split::payeeId),
            action = getAttributeValue(parser, Split::action),
            number = getAttributeValue(parser, Split::number),
            memo = getAttributeValue(parser, Split::memo),
            bankId = getAttributeValue(parser, Split::bankId),
            tagIds = tagIds,
            extra = extraData
        )

        parseChildren(parser) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.Tag ->
                    tagIds.add(
                        getAttributeValue(xmlParser, "id")
                    )
                XmlTags.KeyValuePairs ->
                    extraData.putAll(readKeyValuePairs(xmlParser))
                else ->
                    throw XmlParseException(tagName, "Unknown tag name ${tagName.tagName} found in split. Line ${xmlParser.lineNumber}")
            }
        }

        return split
    }
}
