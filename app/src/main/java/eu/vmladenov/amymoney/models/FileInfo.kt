package eu.vmladenov.amymoney.models

import java.util.*

data class FileInfo(
    val creationDate: Date,
    val lastModificationDate: Date,
    val version: Int,
    val fixVersion: Int
)