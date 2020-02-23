package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlPayeesHandler {
    fun read(parser: XmlPullParser): Payees
}

class XmlPayeesHandler @Inject constructor(): XmlBaseHandler(), IXmlPayeesHandler {
    override fun read(parser: XmlPullParser): Payees {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Payees.tagName)
        val payees = mutableListOf<Payee>()

        parseChildren(parser, XmlTags.Payees) { tagName, xmlParser ->
            if (tagName == XmlTags.Payee) {
                payees.add(readPayee(xmlParser))
            }
        }

        return Payees(payees)
    }

    private fun readPayee(parser: XmlPullParser): Payee {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Payee.tagName)

        val id: String = getAttributeValue(parser, "id")
        val name: String = getAttributeValue(parser, "name")
        val email: String = getAttributeValue(parser, "email")
        val notes: String = getAttributeValue(parser, "notes")
        val reference: String = getAttributeValue(parser, "reference")
        val defaultAccountId: String = getAttributeValue(parser, "defaultaccountid")
        val isMatchingEnabled: Boolean = getAttributeValue(parser, "matchingenabled") == "1"
        val isUsingMatchKey: Boolean = getAttributeValue(parser, "usingmatchkey") == "1"
        val isMatchKeyIgnoreCase: Boolean = getAttributeValue(parser, "matchignorecase") == "1"
        val matchKey: String = getAttributeValue(parser, "matchkey")

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
                                iban = getAttributeValue(xmlParser, "iban"),
                                bic =  getAttributeValue(xmlParser, "bic"),
                                ownerName = getAttributeValue(xmlParser, "ownerName")
                            )
                        )
                        PayeeIdentifierType.NationalAccount.id -> identifiers.add(
                            NationalAccountPayeeIdentifier(
                                bankCode = getAttributeValue(xmlParser, "bankcode"),
                                accountNumber = getAttributeValue(xmlParser, "accountnumber"),
                                country = getAttributeValue(xmlParser, "country"),
                                ownerName = getAttributeValue(xmlParser, "ownername")
                            )
                        )
                    }
                }
                else -> throw ParseException(tagName, "Unknown tagName: ${tagName.tagName} in Payee Tag. Line: ${xmlParser.lineNumber}")
            }
        }

        if (address == null) {
            throw ParseException(XmlTags.Address, "No address found for Payee. Line: ${parser.lineNumber}")
        }
        return Payee(id, name, email, notes, reference, defaultAccountId, isMatchingEnabled, isUsingMatchKey, isMatchKeyIgnoreCase, matchKey, address!!, identifiers)
    }
}
