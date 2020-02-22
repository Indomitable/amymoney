package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Address
import eu.vmladenov.amymoney.models.User
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlUserHandler {
    fun read(parser: XmlPullParser): User
}

class XmlUserHandler @Inject constructor() : XmlBaseHandler(), IXmlUserHandler {
    override fun read(parser: XmlPullParser): User {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.User.tagName)
        val name: String = getAttributeValue(parser, "name")
        val email: String = getAttributeValue(parser, "email")

        val address = readChild(parser, XmlTags.User, XmlTags.Address) {
            Address(
                city = getAttributeValue(it, "city"),
                country = getAttributeValue(it, "county"),
                postCode = getAttributeValue(it, "zipcode"),
                street = getAttributeValue(it, "street"),
                telephone = getAttributeValue(it, "telephone")
            )
        }

        return User(name, email, address)
    }
}
