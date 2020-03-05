package eu.vmladenov.amymoney.ui.views.accounts

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.ui.views.DisposableFragment

class AccountsFragment : DisposableFragment() {
    private val viewModel: AccountsViewModel by viewModels(factoryProducer = { AccountsViewModelFactory() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.accounts_fragment, container, false)
        val institutionsView = view.findViewById<Spinner>(R.id.accountInstitutionsSpinner)

        with (institutionsView) {
            adapter = ArrayAdapter<Institution>(context, android.R.layout.simple_spinner_dropdown_item)
        }

        val accountsView = view.findViewById<RecyclerView>(R.id.accountsList)

        with(accountsView) {
            layoutManager = LinearLayoutManager(context)
            adapter = AccountsAdapter(object : AccountClickHandler {
                override fun handle(account: Account) {

                }
            })
        }

        viewModel.accounts.takeUntil(destroyNotifier).observe(viewLifecycleOwner, Observer {
            (accountsView.adapter as AccountsAdapter).submitList(it)
        })
        return view
    }


    companion object {

        @JvmStatic
        fun newInstance(columnCount: Int) =
            AccountsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}

interface AccountClickHandler {
    fun handle(account: Account)
}
