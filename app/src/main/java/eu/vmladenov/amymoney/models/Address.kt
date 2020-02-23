package eu.vmladenov.amymoney.models

data class Address(
    var city: String = "",
    var country: String = "",  // MyMoneyPayee::state
    var postCode: String = "", // In User stored as zipcode
    var street: String = "",   // MyMoneyPayee::address
    var telephone: String = ""
)
