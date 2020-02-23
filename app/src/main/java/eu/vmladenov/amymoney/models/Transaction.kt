package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.util.*

enum class ReconciledState(val value: Int) {
    Unknown(-1),
    NotReconciled(0),
    Cleared(1),
    Reconciled(2),
    Frozen(3)
}

@XmlTag(XmlTags.Split)
data class Split(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("shares") val shares: String,
    @XmlAttribute("price") val price: String,
    @XmlAttribute("value") val value: String,
    @XmlAttribute("account") val accountId: String,
    @XmlAttribute("") val constCenterId: String,
    @XmlAttribute("reconcileflag") val reconcileFlag: ReconciledState,
    @XmlAttribute("reconciledate") val reconcileDate: Date?,
    @XmlAttribute("payee") val payeeId: String,
    val tags: Tags,
    @XmlAttribute("action") val action: String,
    @XmlAttribute("number") val number: String,
    val bankId: String,
    val transactionId: String
)

@XmlTag(XmlTags.Splits)
@XmlCollection(Split::class)
data class Splits(val splits: List<Split>) : ArrayList<Split>(splits)

@XmlTag(XmlTags.Transaction)
data class Transaction(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("entrydate") val entryDate: Date,
    @XmlAttribute("postdate") val postDate: Date,
    @XmlAttribute("memo") val memo: String,
    @XmlAttribute("commodity") val commodity: String,
    val bankId: String,
    val extra: Map<String, String>
)

@XmlTag(XmlTags.Transactions)
@XmlCollection(Transaction::class)
data class Transactions(val transactions: List<Transaction>) : ArrayList<Transaction>(transactions)


//<TRANSACTION id="T000000000000000001" commodity="EUR" entrydate="2020-02-07" postdate="2018-03-28" memo="">
//<SPLITS>
//<SPLIT reconciledate="" id="S0001" account="A000136" reconcileflag="0" value="-10990/1" bankid="" price="1/1" payee="" memo="" action="" shares="-10990/1" number=""/>
//<SPLIT reconciledate="" id="S0002" account="A000128" reconcileflag="0" value="10990/1" bankid="" price="1/1" payee="" memo="" action="" shares="10990/1" number=""/>
//</SPLITS>
//</TRANSACTION>
