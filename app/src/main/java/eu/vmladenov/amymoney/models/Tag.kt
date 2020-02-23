package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags

@XmlTag(XmlTags.Tag)
data class Tag(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("name") val name: String,
    @XmlAttribute("closed") val closed: Boolean,
    @XmlAttribute("tagcolor") val tagColor: String,
    @XmlAttribute("notes") val notes: String
)

@XmlTag(XmlTags.Tags)
@XmlCollection(Tag::class)
data class Tags(val tags: List<Tag>): ArrayList<Tag>(tags)
