package eu.vmladenov.amymoney.storage.xml

enum class XmlTags(val tagName: String) {
    Unknown(""),
    FileInfo("FILEINFO"),
    CreationDate("CREATION_DATE"),
    LastModifiedDate("LAST_MODIFIED_DATE"),
    Version("VERSION"),
    FixVersion("FIXVERSION"),
    User("USER"),
    Address("ADDRESS"),
    Payee("PAYEE");

    companion object {
        private val map = values().associateBy(XmlTags::tagName)

        operator fun get(tagName: String?): XmlTags {
            if (!map.containsKey(tagName)) {
                return Unknown
            }
            return map[tagName]!!
        }
    }
}


