package eu.vmladenov.amymoney.models

import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.storage.xml.XmlTags
import java.util.*

enum class ReconciledState(val value: Int) {
    Unknown(-1),
    NotReconciled(0),
    Cleared(1),
    Reconciled(2),
    Frozen(3);

    companion object {
        private val map = values().associateBy(ReconciledState::value)

        fun fromAttribute(value: String): ReconciledState {
            return if (value == "") Unknown else get(Integer.parseInt(value))
        }

        operator fun get(value: Int): ReconciledState {
            return map.getOrElse(value) { Unknown }
        }
    }
}

@XmlTag(XmlTags.Split)
data class Split(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("shares") val shares: Fraction,
    @XmlAttribute("price") val price: Fraction,
    @XmlAttribute("value") val value: Fraction,
    @XmlAttribute("account") val accountId: String,
    @XmlAttribute("costcenter") val constCenterId: String,
    @XmlAttribute("reconcileflag") val reconcileFlag: ReconciledState,
    @XmlAttribute("reconciledate") val reconcileDate: Date?,
    @XmlAttribute("payee") val payeeId: String,
    @XmlAttribute("memo") val memo: String,
    val tagIds: List<String>,
    @XmlAttribute("action") val action: String,
    @XmlAttribute("number") val number: String,
    @XmlAttribute("bankid") val bankId: String,
    val extra: Map<String, String>
)

@XmlTag(XmlTags.Splits)
@XmlCollection(Split::class)
class Splits : ArrayList<Split>(emptyList())

@XmlTag(XmlTags.Transaction)
data class Transaction(
    @XmlAttribute("id") val id: String,
    @XmlAttribute("entrydate") val entryDate: Date?,
    @XmlAttribute("postdate") val postDate: Date?,
    @XmlAttribute("memo") val memo: String,
    @XmlAttribute("commodity") val commodity: String,
    @XmlAttribute("bankid") val bankId: String,
    val splits: Splits,
    val extra: Map<String, String>
)

@XmlTag(XmlTags.Transactions)
@XmlCollection(Transaction::class)
data class Transactions(val transactions: List<Transaction>) : ArrayList<Transaction>(transactions)
