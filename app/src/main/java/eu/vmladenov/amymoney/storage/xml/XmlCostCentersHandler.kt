package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.CostCenter
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XmlCostCentersHandler @Inject constructor(): XmlBaseModelCollectionHandler<CostCenter>(XmlTags.CostCenters, XmlTags.CostCenter) {
    override fun update(parser: XmlPullParser, file: XmlFile) {
        file.costCenters.fill(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): CostCenter {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.CostCenter.tagName)
        checkUnsupportedAttributes(parser, CostCenter::class)
        return CostCenter(
            getAttributeValue(parser, CostCenter::id),
            getAttributeValue(parser, CostCenter::name)
        )
    }
}
