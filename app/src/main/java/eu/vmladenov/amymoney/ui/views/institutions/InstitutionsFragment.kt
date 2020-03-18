package eu.vmladenov.amymoney.ui.views.institutions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import kotlinx.android.synthetic.main.institutions_fragment.view.*

class InstitutionsFragment : NavigationFragment() {

    companion object {
        fun newInstance() = InstitutionsFragment()
    }

    private val viewModel: InstitutionsViewModel by viewModels(factoryProducer = { InstitutionsViewModel.Factory() })

    private val adapter: InstitutionsAdapter = InstitutionsAdapter(object : InstitutionClickHandler {
        override fun handle(institution: Institution) {
            val navController = getNavController()
            val action = InstitutionsFragmentDirections.actionNavInstitutionsToAccounts(institution.id)
            navController.navigate(action)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.institutions_fragment, container, false)

        viewModel.institutions
            .takeUntil(destroyNotifier)
            .subscribe {
                adapter.submitList(it)
            }

        with (view.institutionsList) {
            adapter = adapter
        }
        return view
    }
}



