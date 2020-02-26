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
    Payees("PAYEES"),
    Payee("PAYEE"),
    Institutions("INSTITUTIONS"),
    Institution("INSTITUTION"),
    AccountIds("ACCOUNTIDS"),
    AccountId("ACCOUNTID"),
    KeyValuePairs("KEYVALUEPAIRS"),
    Pair("PAIR"),
    PayeeIdentifier("payeeIdentifier"),
    CostCenters("COSTCENTERS"),
    CostCenter("COSTCENTER"),
    Tags("TAGS"),
    Tag("TAG"),
    Account("ACCOUNT"),
    Accounts("ACCOUNTS"),
    SubAccount("SUBACCOUNT"),
    SubAccounts("SUBACCOUNTS"),
    Transactions("TRANSACTIONS"),
    Transaction("TRANSACTION"),
    Splits("SPLITS"),
    Split("SPLIT"),
    Currencies("CURRENCIES"),
    Currency("CURRENCY"),
    Securities("SECURITIES"),
    Security("SECURITY"),
    Prices("PRICES"),
    PricePair("PRICEPAIR"),
    Price("PRICE");

    companion object {
        private val map = values().associateBy(XmlTags::tagName)

        operator fun get(tagName: String?): XmlTags {
            return if (tagName === null) Unknown else map.getOrElse(tagName) { Unknown }
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
