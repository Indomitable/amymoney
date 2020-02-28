package eu.vmladenov.amymoney

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Xml
import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.models.KMyMoneyModel
import eu.vmladenov.amymoney.models.Transaction
import eu.vmladenov.amymoney.storage.xml.IXmlFileHandler
import eu.vmladenov.amymoney.ui.BaseActivity
import eu.vmladenov.amymoney.ui.EmptyAppStateActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.zip.GZIPInputStream

class MainActivity: BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!application.state.isInitialized) {
            val intent = Intent(this, EmptyAppStateActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_main)
            val ammount = displayAmmount()
            textView2.text = ammount.toDecimal().toString()
        }
    }

    fun displayAmmount(): Fraction {
        val model = application.state.model!!
        val amount = model.transactions
            .filter { t -> t.postDate!! < GregorianCalendar().time && t.splits.any { s-> s.accountId == "A000001" }  }
            .fold(Fraction(0, 1)) { acc: Fraction, transaction: Transaction ->
            val split = transaction.splits.find {it.accountId == "A000001" }!!
            return@fold acc + split.value
        }
        return amount
    }
}
