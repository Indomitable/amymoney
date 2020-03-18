package eu.vmladenov.amymoney.ui.views.payees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Payee
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import kotlinx.android.synthetic.main.fragment_payees.view.*

class PayeesFragment : NavigationFragment() {

    private val viewModel by viewModels<PayeesViewModel>{ PayeesViewModel.Factory() }
    private val payeesAdapter = PayeesAdapter(object: PayeeClickHandler {
        override fun handle(payee: Payee) {

        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payees, container, false)

        viewModel.payees
            .takeUntil(destroyNotifier)
            .subscribe {
                payeesAdapter.updateItems(it)
            }

        with(view.payeesList) {
            adapter = payeesAdapter
        }

        return view
    }
}
