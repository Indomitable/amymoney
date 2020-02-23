package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.util.*

@XmlTag(XmlTags.FileInfo)
data class FileInfo(
    val creationDate: Date,
    val lastModificationDate: Date,
    val version: Int,
    val fixVersion: Int
)