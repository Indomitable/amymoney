package eu.vmladenov.amymoney.ui.views.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.ui.views.DisposableFragment
import io.reactivex.rxjava3.functions.Consumer

class AccountsFragment : DisposableFragment() {
    private val viewModel: AccountsViewModel by viewModels(factoryProducer = { AccountsViewModelFactory() })
    private lateinit var institutionsAdapter: InstitutionsSpinnerAdapter
    private lateinit var accountsAdapter: AccountsAdapter
    private lateinit var institutionsView: Spinner
    private lateinit var accountsView: RecyclerView

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

        initViews(view)
        bindToViewModel()
        setEvents()

        return view
    }

    private fun initViews(view: View) {
        institutionsView = view.findViewById(R.id.accountInstitutionsSpinner)

        institutionsAdapter = InstitutionsSpinnerAdapter()
            //ArrayAdapter<Institution>(institutionsView.context, android.R.layout.simple_spinner_dropdown_item)

        institutionsView.adapter = institutionsAdapter

        accountsView = view.findViewById(R.id.accountsList)
        accountsAdapter = AccountsAdapter()
        accountsView.layoutManager = LinearLayoutManager(context)
        accountsView.adapter = accountsAdapter
    }

    private fun bindToViewModel() {
        viewModel.institutions.takeUntil(destroyNotifier).subscribe(Consumer {
            institutionsAdapter.fill(it)
        })

        viewModel.accounts.takeUntil(destroyNotifier).subscribe(Consumer {
            accountsAdapter.submitList(it.toList())
        })
    }

    private fun setEvents() {
        accountsAdapter.clickHandler = object : AccountClickHandler {
            override fun handle(account: Account) {

            }
        }

        institutionsView.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.selectedInstitution.onNext(null)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedInstitution = institutionsAdapter.getItem(position)
                viewModel.selectInstitution(selectedInstitution)
            }
        }
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
