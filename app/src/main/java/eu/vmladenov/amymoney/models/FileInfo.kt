package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.util.*

@XmlTag(XmlTags.FileInfo)
class FileInfo(
    val creationDate: Date = GregorianCalendar().time,
    val lastModificationDate: Date = GregorianCalendar().time,
    val version: Int = -1,
    val fixVersion: Int = -1
) {
    fun isEmpty() = version == -1
}
