package eu.vmladenov.amymoney

import java.util.*

data class FileInfo(val creationDate: Date,
                    val lastModificationDate: Date,
                    val version: Int,
                    val fixVersion: Int)

data class Address(val country: String,
                   val zipCode: String,
                   val street: String,
                   val city: String,
                   val telephone: String)

data class User(val email: String,
                val name: String,
                val address: Address)






data class KMyMoneyFile(val fileInfo: FileInfo,
                        val user: User)