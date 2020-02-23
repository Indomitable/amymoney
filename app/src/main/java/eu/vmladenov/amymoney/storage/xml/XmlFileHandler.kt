package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.*
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlFileHandler {
    fun read(parser: XmlPullParser): KMyMoneyState
}

class XmlFileHandler @Inject constructor (
    private val fileInfoHandler: IXmlFileInfoHandler,
    private val userHandler: IXmlUserHandler,
    private val institutionsHandler: IXmlInstitutionsHandler,
    private val payeesHandler: IXmlPayeesHandler
): XmlBaseHandler(), IXmlFileHandler {

    override fun read(parser: XmlPullParser): KMyMoneyState {
        var eventType = parser.eventType
        var fileInfo: FileInfo? = null
        var user: User? = null
        var institutions: Institutions? = null
        var payees: Payees? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (XmlTags[parser.name]) {
                        XmlTags.FileInfo -> fileInfo = fileInfoHandler.read(parser)
                        XmlTags.User -> user = userHandler.read(parser)
                        XmlTags.Institutions -> institutions = institutionsHandler.read(parser)
                        XmlTags.Payees -> payees = payeesHandler.read(parser)
                    }
                }
            }
            eventType = parser.next()
        }

        if (fileInfo == null) {
            throw ParseException(XmlTags.FileInfo, "No File info tag is found")
        }
        if (user == null) {
            throw ParseException(XmlTags.User, "No User tag is found")
        }
        if (institutions == null) {
            institutions = Institutions(emptyList())
        }
        if (payees == null) {
            payees = Payees(emptyList())
        }

        return KMyMoneyState(fileInfo, user, institutions, payees)
    }
}