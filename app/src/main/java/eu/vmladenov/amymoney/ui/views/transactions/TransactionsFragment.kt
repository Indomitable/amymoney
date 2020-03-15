package eu.vmladenov.amymoney.ui.views.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Transaction
import eu.vmladenov.amymoney.ui.views.NavigationFragment

class TransactionsFragment : NavigationFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

}


interface TransactionClickHandler {
    fun handle(transaction: Transaction)
}
