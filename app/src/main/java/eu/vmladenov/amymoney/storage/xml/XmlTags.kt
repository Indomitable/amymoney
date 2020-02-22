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

enum class XmlAddressAttributes {
    Street,
    City,
    PostCode,
    Country,
    State,
    Telephone,
}

enum class XmlPayeeAttributes {
    ID,
    Name,
    Type,
    Reference,
    Notes,
    MatchingEnabled,
    UsingMatchKey,
    MatchIgnoreCase,
    MatchKey,
    DefaultAccountID,
    Email,
    IBAN,
    BIC,
    OwnerVer1,
    OwnerVer2,
    BankCode,
    AccountNumber,
}

enum class XmlInstitutionAttributes {
    ID,
    Name,
    Manager,
    SortCode,
    Street,
    City,
    Zip,
    Telephone,
}
