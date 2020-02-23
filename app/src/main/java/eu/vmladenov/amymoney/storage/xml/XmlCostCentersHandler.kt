package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlCostCentersHandler {
    fun read(parser: XmlPullParser): CostCenters
}

class XmlCostCentersHandler @Inject constructor(): XmlBaseCollectionHandler<CostCenter>(XmlTags.CostCenters, XmlTags.CostCenter), IXmlCostCentersHandler {
    override fun read(parser: XmlPullParser): CostCenters {
        return CostCenters(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): CostCenter {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.CostCenter.tagName)
        return CostCenter(
            getAttributeValue(parser, CostCenter::id),
            getAttributeValue(parser, CostCenter::name)
        )
    }
}
