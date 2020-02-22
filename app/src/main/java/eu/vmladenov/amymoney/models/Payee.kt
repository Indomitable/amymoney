package eu.vmladenov.amymoney.models

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
    val address: Address = Address()
)