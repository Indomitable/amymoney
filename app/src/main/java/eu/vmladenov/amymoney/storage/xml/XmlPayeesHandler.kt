package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlPayeesHandler {
    fun read(parser: XmlPullParser): Payees
}

class XmlPayeesHandler @Inject constructor(): XmlBaseCollectionHandler<Payee>(XmlTags.Payees, XmlTags.Payee), IXmlPayeesHandler {
    override fun read(parser: XmlPullParser): Payees {
        return Payees(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Payee {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Payee.tagName)

        val id: String = getAttributeValue(parser, Payee::id)
        val name: String = getAttributeValue(parser, Payee::name)
        val email: String = getAttributeValue(parser, Payee::email)
        val notes: String = getAttributeValue(parser, Payee::notes)
        val reference: String = getAttributeValue(parser, Payee::reference)
        val defaultAccountId: String = getAttributeValue(parser, Payee::defaultAccountId)
        val isMatchingEnabled: Boolean = getAttributeValue(parser, Payee::isMatchingEnabled) == "1"
        val isUsingMatchKey: Boolean = getAttributeValue(parser, Payee::isUsingMatchKey) == "1"
        val isMatchKeyIgnoreCase: Boolean = getAttributeValue(parser, Payee::isMatchKeyIgnoreCase) == "1"
        val matchKey: String = getAttributeValue(parser, Payee::matchKey)

        var address: Address? = null
        val identifiers: MutableList<IPayeeIdentifier> = mutableListOf()

        parseChildren(parser, XmlTags.Payee) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.Address -> address = Address(
                    city = getAttributeValue(xmlParser, "city"),
                    country = getAttributeValue(xmlParser, "state"),
                    postCode = getAttributeValue(xmlParser, "postcode"),
                    street = getAttributeValue(xmlParser, "street"),
                    telephone = getAttributeValue(xmlParser, "telephone")
                )
                XmlTags.PayeeIdentifier -> {
                    when (getAttributeValue(xmlParser,"type")) {
                        PayeeIdentifierType.IbanBic.id -> identifiers.add(
                            IbanBicPayeeIdentifier(
                                iban = getAttributeValue(xmlParser, IbanBicPayeeIdentifier::iban),
                                bic =  getAttributeValue(xmlParser, IbanBicPayeeIdentifier::bic),
                                ownerName = getAttributeValue(xmlParser, IbanBicPayeeIdentifier::ownerName)
                            )
                        )
                        PayeeIdentifierType.NationalAccount.id -> identifiers.add(
                            NationalAccountPayeeIdentifier(
                                bankCode = getAttributeValue(xmlParser, NationalAccountPayeeIdentifier::bankCode),
                                accountNumber = getAttributeValue(xmlParser, NationalAccountPayeeIdentifier::accountNumber),
                                country = getAttributeValue(xmlParser, NationalAccountPayeeIdentifier::country),
                                ownerName = getAttributeValue(xmlParser, NationalAccountPayeeIdentifier::ownerName)
                            )
                        )
                    }
                }
                else -> throw XmlParseException(tagName, "Unknown tagName: ${tagName.tagName} in Payee Tag. Line: ${xmlParser.lineNumber}")
            }
        }

        if (address == null) {
            throw XmlParseException(XmlTags.Address, "No address found for Payee. Line: ${parser.lineNumber}")
        }
        return Payee(id, name, email, notes, reference, defaultAccountId, isMatchingEnabled, isUsingMatchKey, isMatchKeyIgnoreCase, matchKey, address!!, identifiers)
    }
}
