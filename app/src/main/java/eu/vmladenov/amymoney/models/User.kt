package eu.vmladenov.amymoney.models

data class User (
    val name: String = "",
    val email: String = "",
    val address: Address = Address()
)