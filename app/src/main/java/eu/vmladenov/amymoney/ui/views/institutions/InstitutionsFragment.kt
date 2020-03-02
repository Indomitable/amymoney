package eu.vmladenov.amymoney.ui.views.institutions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.AMyMoneyApplication

import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Institution

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

class InstitutionsViewModelFactory(val institutionsFragment: InstitutionsFragment) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val injector = (institutionsFragment.activity!!.application as AMyMoneyApplication).injector
        return InstitutionsViewModel(injector.getRepository()) as T
    }
}

class InstitutionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(institution: Institution) {
        (itemView as TextView).text = institution.name
    }
}

class InstitutionDiffCallBack: DiffUtil.ItemCallback<Institution>() {
    override fun areItemsTheSame(oldItem: Institution, newItem: Institution): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Institution, newItem: Institution): Boolean {
        return oldItem == newItem
    }
}


class InstitutionsAdapter: ListAdapter<Institution, InstitutionViewHolder>(InstitutionDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstitutionViewHolder {
        return InstitutionViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    override fun onBindViewHolder(holder: InstitutionViewHolder, position: Int) = holder.bind(getItem(position))

}

