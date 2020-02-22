package eu.vmladenov.amymoney.models

data class Address(
    val city: String = "",
    val country: String = "",  // MyMoneyPayee::state
    val postCode: String = "", // In User stored as zipcode
    val street: String = "",   // MyMoneyPayee::address
    val telephone: String = ""
)