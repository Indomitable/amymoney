package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Address
import eu.vmladenov.amymoney.models.User
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject
import javax.inject.Singleton

interface IXmlUserHandler {
    fun read(parser: XmlPullParser): User
}

@Singleton
class XmlUserHandler @Inject constructor() : XmlBaseHandler(), IXmlUserHandler {
    override fun read(parser: XmlPullParser): User {
        parser.require(XmlPullParser.START_TAG, null, XmlTags.User.tagName)
        checkUnsupportedAttributes(parser, User::class)
        return User(
            name = getAttributeValue(parser, User::name),
            email = getAttributeValue(parser, User::email),
            address = readChild(parser, XmlTags.Address) {
                return@readChild Address(
                    city = getAttributeValue(it, "city"),
                    country = getAttributeValue(it, "county"),
                    postCode = getAttributeValue(it, "zipcode"),
                    street = getAttributeValue(it, "street"),
                    telephone = getAttributeValue(it, "telephone")
                )
            }
        )
    }

    override fun update(parser: XmlPullParser, file: XmlFile) {
        file.user = read(parser)
    }
}
