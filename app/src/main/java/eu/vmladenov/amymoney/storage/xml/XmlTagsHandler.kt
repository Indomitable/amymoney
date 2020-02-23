package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlTagsHandler {
    fun read(parser: XmlPullParser): Tags
}

class XmlTagsHandler @Inject constructor(): XmlBaseCollectionHandler<Tag>(XmlTags.Tags, XmlTags.Tag), IXmlTagsHandler {
    override fun read(parser: XmlPullParser): Tags {
        return Tags(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Tag {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Tag.tagName)
        return Tag(
            getAttributeValue(parser, Tag::id),
            getAttributeValue(parser, Tag::name),
            getAttributeValue(parser, Tag::closed) == "1",
            getAttributeValue(parser, Tag::tagColor),
            getAttributeValue(parser, Tag::notes)
        )
    }
}
