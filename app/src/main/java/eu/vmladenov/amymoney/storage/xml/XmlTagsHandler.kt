package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlTagsHandler @Inject constructor(): XmlBaseCollectionHandler<Tag>(XmlTags.Tags, XmlTags.Tag) {
    override fun update(parser: XmlPullParser, repository: IAMyMoneyRepository) {
        repository.tags.fill(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Tag {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Tag.tagName)
        checkUnsupportedAttributes(parser, Tag::class)

        return Tag(
            getAttributeValue(parser, Tag::id),
            getAttributeValue(parser, Tag::name),
            getAttributeValue(parser, Tag::closed) == "1",
            getAttributeValue(parser, Tag::tagColor),
            getAttributeValue(parser, Tag::notes)
        )
    }
}
