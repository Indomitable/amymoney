package eu.vmladenov.amymoney.storage.xml

import org.xmlpull.v1.XmlPullParser

abstract class XmlBaseCollectionHandler<TChild>(private val parentTag: XmlTags, private val childTag: XmlTags): XmlBaseHandler() {

    protected fun readChildren(parser: XmlPullParser): List<TChild> {
        parser.require(XmlPullParser.START_TAG, null, parentTag.tagName)
        val children = mutableListOf<TChild>()

        parseChildren(parser) { tagName, xmlParser ->
            if (tagName == childTag) {
                children.add(readChild(xmlParser))
            }
        }
        return children
    }

    protected abstract fun readChild(parser: XmlPullParser): TChild
}
