package eu.vmladenov.amymoney.models

data class Payees(val payees: List<Payee>): ArrayList<Payee>(payees)

data class Payee (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val notes: String = "",
    val reference: String = "",
    val defaultAccountId: String = "",
    val isMatchingEnabled: Boolean = true,
    val isUsingMatchKey: Boolean = true,
    val isMatchKeyIgnoreCase: Boolean = true,
    val matchKey: String = "",
    val address: Address = Address(),
    val identifiers: List<IPayeeIdentifier> = emptyList()
)

enum class PayeeIdentifierType(val id: String) {
    IbanBic("org.kmymoney.payeeIdentifier.ibanbic"),
    NationalAccount("org.kmymoney.payeeIdentifier.national")
}

interface IPayeeIdentifier {
    val type: PayeeIdentifierType
}


data class IbanBicPayeeIdentifier(val iban: String, val bic: String, val ownerName: String): IPayeeIdentifier {
    override val type: PayeeIdentifierType = PayeeIdentifierType.IbanBic
}

data class NationalAccountPayeeIdentifier(val bankCode: String, val accountNumber: String, val ownerName: String, val country: String): IPayeeIdentifier {
    override val type: PayeeIdentifierType = PayeeIdentifierType.NationalAccount
}
