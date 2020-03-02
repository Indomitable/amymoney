package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags

@XmlTag(XmlTags.User)
data class User (
    @XmlAttribute("name") val name: String = "",
    @XmlAttribute("email") val email: String = "",
    val address: Address = Address()
)
