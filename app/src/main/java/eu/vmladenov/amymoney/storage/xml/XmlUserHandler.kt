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
        val user = User(
            name = getAttributeValue(parser, User::name),
            email = getAttributeValue(parser, User::email),
            address = Address()
        )
        readChild(parser, XmlTags.Address) {
            user.address.city = getAttributeValue(it, "city")
            user.address.country = getAttributeValue(it, "county")
            user.address.postCode = getAttributeValue(it, "zipcode")
            user.address.street = getAttributeValue(it, "street")
            user.address.telephone = getAttributeValue(it, "telephone")
        }
        return user
    }
}
