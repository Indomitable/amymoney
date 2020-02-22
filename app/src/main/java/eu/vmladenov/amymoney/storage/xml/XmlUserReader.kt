package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.Address
import eu.vmladenov.amymoney.User
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlUserReader {
    fun read(): User
}

class XmlUserReader @Inject constructor(parser: XmlPullParser) : XmlBaseReader(parser), IXmlUserReader {
    override fun read(): User {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.User.tagName)
        val name: String = parser.getAttributeValue(null, "name")
        val email: String = parser.getAttributeValue(null, "email") ?: ""

        parser.nextTag()
        parser.require(XmlPullParser.START_TAG, null, XmlTags.Address.tagName)

        val address = Address(
            city = parser.getAttributeValue(null, "city") ?: "",
            country = parser.getAttributeValue(null, "county") ?: "",
            postCode = parser.getAttributeValue(null, "zipcode") ?: "",
            street = parser.getAttributeValue(null, "street") ?: "",
            telephone = parser.getAttributeValue(null, "telephone") ?: ""
        )
        parser.nextTag() // </ADDRESS>
        parser.nextTag() // </USER>
        return User(name, email, address)
    }
}
