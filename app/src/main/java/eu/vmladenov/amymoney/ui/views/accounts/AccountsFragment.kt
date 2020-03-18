package eu.vmladenov.amymoney.ui.views.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import eu.vmladenov.amymoney.ui.views.transactions.TransactionsFragment
import kotlinx.android.synthetic.main.accounts_fragment.view.*

class AccountsFragment : NavigationFragment() {
    private lateinit var viewModel: AccountsViewModel
    private lateinit var institutionsAdapter: InstitutionsSpinnerAdapter
    private val accountsAdapter = AccountsAdapter(object : AccountClickHandler {
        override fun handle(account: Account) {
            val navController = getNavController()
            val action = AccountsFragmentDirections.actionNavAccountsToTransactions(account.id)
            navController.navigate(action)
        }
    })
    private lateinit var institutionsView: Spinner
    private lateinit var accountsView: RecyclerView

    private var initialInstitutionId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            initialInstitutionId = it.getString(InstitutionIdArg, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, AccountsViewModel.Factory(initialInstitutionId)).get(AccountsViewModel::class.java)
            .also {
                bindToViewModel(it)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.accounts_fragment, container, false)

        initViews(view)
        setEvents()

        return view
    }

    private fun initViews(view: View) {
        institutionsView = view.accountInstitutionsSpinner

        institutionsAdapter = InstitutionsSpinnerAdapter()

        institutionsView.adapter = institutionsAdapter

        accountsView = view.accountsList
        accountsView.layoutManager = LinearLayoutManager(context)
        accountsView.adapter = accountsAdapter
    }

    private fun bindToViewModel(viewModel: AccountsViewModel) {
        viewModel.institutions.takeUntil(destroyNotifier).subscribe {
            val selectedItem = viewModel.selectedInstitution
            institutionsAdapter.fill(it)
            val index = institutionsAdapter.getPosition(selectedItem)
            if (index >= 0) {
                institutionsView.setSelection(index)
            }
        }

        viewModel.accounts.takeUntil(destroyNotifier).subscribe {
            accountsAdapter.submitList(it.toList())
        }
    }

    private fun setEvents() {
        institutionsView.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.selectedInstitution = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectedInstitution = institutionsAdapter.getItem(position)
            }
        }
    }

    companion object {
        const val InstitutionIdArg = "INSTITUTION_ID"

        @JvmStatic
        fun newInstance(institutionId: String) =
            AccountsFragment().apply {
                arguments = Bundle().apply {
                    putString(InstitutionIdArg, institutionId)
                }
            }
    }
}

interface AccountClickHandler {
    fun handle(account: Account)
}
