package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags

@XmlTag(XmlTags.Institution)
data class Institution(
    @XmlAttribute("id") override val id: String = "",
    @XmlAttribute("name") val name: String = "",
    @XmlAttribute("sortcode") val sortCode: String = "",
    @XmlAttribute("manager") val manager: String = "",
    val address: Address = Address(),
    val accountIds: List<String> = emptyList(),
    val extra: Map<String, String> = emptyMap()
): IModel

@XmlTag(XmlTags.Institutions)
@XmlCollection(Institution::class)
class Institutions: BaseList<Institution>()

object InstitutionComparable: Comparable<Institution>()
