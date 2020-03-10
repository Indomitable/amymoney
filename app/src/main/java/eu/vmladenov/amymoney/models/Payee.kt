package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags

@XmlTag(XmlTags.Payee)
data class Payee(
    @XmlAttribute("id") override val id: String = "",
    @XmlAttribute("name") val name: String = "",
    @XmlAttribute("email") val email: String = "",
    @XmlAttribute("notes") val notes: String = "",
    @XmlAttribute("reference") val reference: String = "",
    @XmlAttribute("defaultaccountid") val defaultAccountId: String = "",
    @XmlAttribute("matchingenabled") val isMatchingEnabled: Boolean = true,
    @XmlAttribute("usingmatchkey") val isUsingMatchKey: Boolean = true,
    @XmlAttribute("matchignorecase") val isMatchKeyIgnoreCase: Boolean = true,
    @XmlAttribute("matchkey") val matchKey: String = "",
    val address: Address = Address(),
    val identifiers: List<IPayeeIdentifier> = emptyList()
): IModel

@XmlTag(XmlTags.Payees)
@XmlCollection(Payee::class)
class Payees(items: Map<String, Payee> = emptyMap()) : BaseMap<Payee>(items)

enum class PayeeIdentifierType(val id: String) {
    IbanBic("org.kmymoney.payeeIdentifier.ibanbic"),
    NationalAccount("org.kmymoney.payeeIdentifier.national")
}

interface IPayeeIdentifier {
    @XmlAttribute("type")
    val type: PayeeIdentifierType
}

data class IbanBicPayeeIdentifier(
    @XmlAttribute("iban") val iban: String,
    @XmlAttribute("bic") val bic: String,
    @XmlAttribute("ownerName") val ownerName: String
) : IPayeeIdentifier {
    override val type: PayeeIdentifierType = PayeeIdentifierType.IbanBic
}

data class NationalAccountPayeeIdentifier(
    @XmlAttribute("bankcode") val bankCode: String,
    @XmlAttribute("accountnumber") val accountNumber: String,
    @XmlAttribute("ownername") val ownerName: String,
    @XmlAttribute("country") val country: String
) : IPayeeIdentifier {
    override val type: PayeeIdentifierType = PayeeIdentifierType.NationalAccount
}
