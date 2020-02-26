package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

interface IXmlCostCentersHandler {
    fun read(parser: XmlPullParser): CostCenters
}

@Singleton
class XmlCostCentersHandler @Inject constructor(): XmlBaseCollectionHandler<CostCenter>(XmlTags.CostCenters, XmlTags.CostCenter), IXmlCostCentersHandler {
    override fun read(parser: XmlPullParser): CostCenters {
        return CostCenters(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): CostCenter {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.CostCenter.tagName)
        checkUnsupportedAttributes(parser, CostCenter::class)
        return CostCenter(
            getAttributeValue(parser, CostCenter::id),
            getAttributeValue(parser, CostCenter::name)
        )
    }

    override fun update(parser: XmlPullParser, file: KMyMoneyFile) {
        file.costCenters = read(parser)
    }
}
