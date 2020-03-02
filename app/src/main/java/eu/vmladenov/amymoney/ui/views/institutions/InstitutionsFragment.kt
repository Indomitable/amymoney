package eu.vmladenov.amymoney.ui.views.institutions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.models.InstitutionComparable

class InstitutionsFragment : Fragment() {

    companion object {
        fun newInstance() = InstitutionsFragment()
    }

    private val viewModel: InstitutionsViewModel by viewModels(factoryProducer = { InstitutionsViewModelFactory(this) } )
    private lateinit var adapter: InstitutionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.institutions_fragment, container, false)
        adapter = InstitutionsAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.institutionsList)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        viewModel.institutions.observe (viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}


class InstitutionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(institution: Institution) {
        (itemView as TextView).text = institution.name
    }
}

class InstitutionsAdapter: ListAdapter<Institution, InstitutionViewHolder>(InstitutionComparable) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstitutionViewHolder {
        return InstitutionViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: InstitutionViewHolder, position: Int) = holder.bind(getItem(position))
}

