package eu.vmladenov.amymoney.storage.xml

import eu.vmladenov.amymoney.models.Transaction
import eu.vmladenov.amymoney.models.Transactions
import org.xmlpull.v1.XmlPullParser
import javax.inject.Inject

interface IXmlTransactionsHandler {
    fun read(parser: XmlPullParser): Transactions
}

class XmlTransactionsHandler @Inject constructor(): XmlBaseCollectionHandler<Transaction>(XmlTags.Transactions, XmlTags.Transaction), IXmlTransactionsHandler {
    override fun read(parser: XmlPullParser): Transactions {
        return Transactions(readChildren(parser))
    }

    override fun readChild(parser: XmlPullParser): Transaction {
        throw NotImplementedError()
    }
}
