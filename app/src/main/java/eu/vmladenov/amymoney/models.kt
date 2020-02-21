package eu.vmladenov.amymoney

import java.util.*

data class FileInfo(
    val creationDate: Date,
    val lastModificationDate: Date,
    val version: Int,
    val fixVersion: Int
)

data class Address(
    val city: String = "",
    val country: String = "",  // MyMoneyPayee::state
    val postCode: String = "", // In User stored as zipcode
    val street: String = "",   // MyMoneyPayee::address
    val telephone: String = ""
)

data class Payee(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val notes: String = "",
    val reference: String = "",
    val isMatchingEnabled: Boolean = true,
    val isUsingMatchKey: Boolean = true,
    val isMatchKeyIgnoreCase: Boolean = true,
    val matchKey: String = "",
    val address: Address = Address()
)



data class KMyMoneyFile(
    val fileInfo: FileInfo,
    val user: Payee
)