package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlPayeesHandler @Inject constructor(): XmlBaseModelCollectionHandler<Payee>(XmlTags.Payees, XmlTags.Payee) {

    override fun update(parser: XmlPullParser, file: XmlFile) {
        val payees = readChildrenMap(parser)
        file.payees.fill(payees)
    }

    override fun readChild(parser: XmlPullParser): Payee {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Payee.tagName)
        checkUnsupportedAttributes(parser, Payee::class)

        val identifiers: MutableList<IPayeeIdentifier> = mutableListOf()
        val payee = Payee (
            id = getAttributeValue(parser, Payee::id),
            name = getAttributeValue(parser, Payee::name),
            email = getAttributeValue(parser, Payee::email),
            notes = getAttributeValue(parser, Payee::notes),
            reference = getAttributeValue(parser, Payee::reference),
            defaultAccountId = getAttributeValue(parser, Payee::defaultAccountId),
            isMatchingEnabled = getAttributeValue(parser, Payee::isMatchingEnabled) == "1",
            isUsingMatchKey = getAttributeValue(parser, Payee::isUsingMatchKey) == "1",
            isMatchKeyIgnoreCase = getAttributeValue(parser, Payee::isMatchKeyIgnoreCase) == "1",
            matchKey = getAttributeValue(parser, Payee::matchKey),
            address = Address(),
            identifiers = identifiers
        )

        parseChildren(parser) { tagName, xmlParser ->
            when (tagName) {
                XmlTags.Address -> {
                    payee.address.city = getAttributeValue(xmlParser, "city")
                    payee.address.country = getAttributeValue(xmlParser, "state")
                    payee.address.postCode = getAttributeValue(xmlParser, "postcode")
                    payee.address.street = getAttributeValue(xmlParser, "street")
                    payee.address.telephone = getAttributeValue(xmlParser, "telephone")
                }
                XmlTags.PayeeIdentifier -> {
                    when (getAttributeValue(xmlParser, IPayeeIdentifier::type)) {
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
        return payee
    }
}
