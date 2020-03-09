package eu.vmladenov.amymoney.ui.views.institutions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.ui.views.NavigationFragment

class InstitutionsFragment : NavigationFragment() {

    companion object {
        fun newInstance() = InstitutionsFragment()
    }

    private val viewModel: InstitutionsViewModel by viewModels(factoryProducer = { InstitutionsViewModelFactory() })
    private lateinit var adapter: InstitutionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.institutions_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.institutionsList)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = InstitutionsAdapter(object : InstitutionClickHandler {
            override fun handle(institution: Institution) {
                val navController = getNavController()
                val action = InstitutionsFragmentDirections.actionNavInstitutionsToAccounts(institution.id)
                navController.navigate(action)
            }
        })

        viewModel.institutions.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        recyclerView.adapter = adapter
        return view
    }
}

interface InstitutionClickHandler {
    fun handle(institution: Institution)
}

