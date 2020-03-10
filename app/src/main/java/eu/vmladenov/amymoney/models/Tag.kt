package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags

@XmlTag(XmlTags.Tag)
data class Tag(
    @XmlAttribute("id") override val id: String,
    @XmlAttribute("name") val name: String,
    @XmlAttribute("closed") val closed: Boolean,
    @XmlAttribute("tagcolor") val tagColor: String,
    @XmlAttribute("notes") val notes: String
): IModel

@XmlTag(XmlTags.Tags)
@XmlCollection(Tag::class)
class Tags(items: Map<String, Tag> = emptyMap()): BaseMap<Tag>(items)
