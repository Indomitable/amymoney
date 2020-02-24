package eu.vmladenov.amymoney.android_test.storage.xml

import eu.vmladenov.amymoney.models.ReconciledState
import eu.vmladenov.amymoney.storage.xml.XmlTransactionsHandler
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class XmlTransactionsHandlerTests: BaseXmlHandlerTest() {
    lateinit var service: XmlTransactionsHandler

    @Before
    fun setup() {
        service = XmlTransactionsHandler()
    }

    @Test
    fun shouldBeAbleToParseTransaction() {
        val parser = createParser("""
            <TRANSACTIONS>
              <TRANSACTION memo="Some spendings" entrydate="2020-02-24" postdate="2020-02-25" id="T000000000000000008" commodity="EUR">
               <SPLITS>
                <SPLIT shares="-227/20" reconciledate="" price="1/1" memo="Split 0 memo" number="2" id="S0001" account="A000001" bankid="" value="-227/20" reconcileflag="1" action="An Action" payee="P000003">
                 <TAG id="G000002"/>
                 <TAG id="G000003"/>
                </SPLIT>
                <SPLIT shares="207/20" reconciledate="2020-02-22" price="1/1" memo="" number="" id="S0002" account="A000064" bankid="aaa" value="207/20" reconcileflag="0" action="" payee="P000003"/>
                <SPLIT shares="1/1" reconciledate="" price="1/1" memo="Book" number="" id="S0003" account="A000058" bankid="" value="1/1" reconcileflag="0" action="" payee="P000003">
                 <TAG id="G000004"/>
                </SPLIT>
               </SPLITS>
              </TRANSACTION>
             </TRANSACTIONS>
        """)
        val transactions = service.read(parser)
        Assert.assertEquals(1, transactions.size)
        val transaction = transactions[0]
        Assert.assertEquals("T000000000000000008", transaction.id)
        Assert.assertEquals("Some spendings", transaction.memo)
        Assert.assertEquals(GregorianCalendar(2020, 1, 24).time, transaction.entryDate)
        Assert.assertEquals(GregorianCalendar(2020, 1, 25).time, transaction.postDate)
        Assert.assertEquals("EUR", transaction.commodity)

        val splits = transaction.splits
        Assert.assertEquals(3, splits.size)
        val split0 = splits[0]
        Assert.assertEquals("S0001", split0.id)
        Assert.assertEquals("A000001", split0.accountId)
        Assert.assertEquals("An Action", split0.action)
        Assert.assertEquals("Split 0 memo", split0.memo)
        Assert.assertEquals("2", split0.number)
        Assert.assertEquals("-227/20", split0.shares)
        Assert.assertEquals("1/1", split0.price)
        Assert.assertEquals("-227/20", split0.value)
        Assert.assertEquals("P000003", split0.payeeId)
        Assert.assertNull(split0.reconcileDate)
        Assert.assertEquals(ReconciledState.Cleared, split0.reconcileFlag)

        Assert.assertEquals(2, split0.tagIds.size)
        Assert.assertEquals("G000002", split0.tagIds[0])
        Assert.assertEquals("G000003", split0.tagIds[1])

        val split1 = splits[1]
        Assert.assertEquals("S0002", split1.id)
        Assert.assertEquals(GregorianCalendar(2020, 1, 22).time, split1.reconcileDate)
        Assert.assertEquals("aaa", split1.bankId)
    }

    @Test
    fun shouldHandleMultipleTransactions() {
        val parser = createParser("""
<TRANSACTIONS>
  <TRANSACTION memo="" entrydate="2020-02-22" postdate="2020-02-22" id="T000000000000000006" commodity="EUR">
   <SPLITS>
    <SPLIT shares="-15/1" reconciledate="" price="1/1" memo="Food" number="1" id="S0001" account="A000129" bankid="" value="-15/1" reconcileflag="0" action="" payee="P000003"/>
    <SPLIT shares="15/1" reconciledate="" price="1/1" memo="Food" number="" id="S0002" account="A000064" bankid="" value="15/1" reconcileflag="0" action="" payee="P000003"/>
   </SPLITS>
  </TRANSACTION>
  <TRANSACTION memo="" entrydate="2020-02-22" postdate="2020-02-22" id="T000000000000000007" commodity="EUR">
   <SPLITS>
    <SPLIT shares="2500/1" reconciledate="" price="1/1" memo="" number="1" id="S0001" account="A000001" bankid="" value="2500/1" reconcileflag="1" action="" payee="P000004"/>
    <SPLIT shares="-2500/1" reconciledate="" price="1/1" memo="" number="" id="S0002" account="A000008" bankid="" value="-2500/1" reconcileflag="0" action="" payee="P000004"/>
   </SPLITS>
  </TRANSACTION>
</TRANSACTIONS>
        """)
        val transactions = service.read(parser)
        Assert.assertEquals(2, transactions.size)
        Assert.assertEquals("T000000000000000006", transactions[0].id)
        Assert.assertEquals(2, transactions[0].splits.size)
        Assert.assertEquals("T000000000000000007", transactions[1].id)
        Assert.assertEquals(2, transactions[1].splits.size)
    }

    @Test
    fun shouldHandleEmpty() {
        val parser = createParser("""
<TRANSACTIONS count="0" />
        """)
        val transactions = service.read(parser)
        Assert.assertEquals(0, transactions.size)
    }
}
