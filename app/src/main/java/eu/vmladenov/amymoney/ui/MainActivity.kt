package eu.vmladenov.amymoney.ui

import android.os.Bundle
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.models.Transaction
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ammount = displayAmmount()
        textView2.text = ammount.toDecimal().toString()
    }

    fun displayAmmount(): Fraction {
        val model = application.state.model!!
        val amount = model.transactions
            .filter { t -> t.postDate!! < GregorianCalendar().time && t.splits.any { s -> s.accountId == "A000001" } }
            .fold(Fraction(0, 1)) { acc: Fraction, transaction: Transaction ->
                val split = transaction.splits.find { it.accountId == "A000001" }!!
                val res = (acc + split.value) // We need to simplify because during the process the denominator is growing too much and creates overflow
                if (res.denominator == 0L) {
                    throw Exception("Denominator is zero")
                }
                return@fold res
            }
        return amount
    }
}
