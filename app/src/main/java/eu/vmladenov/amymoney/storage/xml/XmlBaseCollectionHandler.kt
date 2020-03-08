package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.IModel
import org.xmlpull.v1.XmlPullParser

abstract class XmlBaseCollectionHandler<TChild>(protected val parentTag: XmlTags, protected val childTag: XmlTags): XmlBaseHandler() {

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

abstract class XmlBaseModelCollectionHandler<TChild: IModel>(parentTag: XmlTags, childTag: XmlTags): XmlBaseCollectionHandler<TChild>(parentTag, childTag) {
    protected fun readChildrenMap(parser: XmlPullParser): Map<String, TChild> {
        parser.require(XmlPullParser.START_TAG, null, parentTag.tagName)
        val children = mutableMapOf<String, TChild>()

        parseChildren(parser) { tagName, xmlParser ->
            if (tagName == childTag) {
                val child = readChild(xmlParser)
                children[child.id] = child
            }
        }
        return children
    }
}
