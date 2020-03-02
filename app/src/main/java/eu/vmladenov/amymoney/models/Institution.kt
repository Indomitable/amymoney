package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags

@XmlTag(XmlTags.Institution)
data class Institution(
    @XmlAttribute("id") val id: String = "",
    @XmlAttribute("name") val name: String = "",
    @XmlAttribute("sortcode") val sortCode: String = "",
    @XmlAttribute("manager") val manager: String = "",
    val address: Address = Address(),
    val accountIds: List<String>,
    val extra: Map<String, String>
)

@XmlTag(XmlTags.Institutions)
@XmlCollection(Institution::class)
class Institutions: BaseList<Institution>()
