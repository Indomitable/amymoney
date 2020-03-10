package eu.vmladenov.amymoney.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : NavigationFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels(factoryProducer = { HomeViewModelFactory() })

    private lateinit var tvNoData: TextView
    private lateinit var overviewContainer: LinearLayout

    private lateinit var assetsAdapter: AccountBalanceAdapter
    private lateinit var liabilityAdapter: AccountBalanceAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false).also {
            initViews(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeToViewModel(viewModel)
    }

    private fun initViews(view: View) {
        tvNoData = view.tvNoData
        overviewContainer = view.overviewContainer
        assetsAdapter = AccountBalanceAdapter()
        liabilityAdapter = AccountBalanceAdapter()

        with(view.assetAccountsBalance) {
            layoutManager = LinearLayoutManager(context)
            adapter = assetsAdapter
        }

        with(view.liabilityAccountsBalance) {
            layoutManager = LinearLayoutManager(context)
            adapter = liabilityAdapter
        }
    }

    private fun subscribeToViewModel(viewModel: HomeViewModel) {
        with(viewModel) {
            isDataLoaded
                .takeUntil(destroyNotifier)
                .subscribe {
                    tvNoData.visibility = if (it) View.GONE else View.VISIBLE
                    overviewContainer.visibility = if (it) View.VISIBLE else View.GONE
                }

            assetAccountsBalance
                .takeUntil(destroyNotifier)
                .subscribe {
                    assetsAdapter.submitList(it.toList())
                }

            liabilityAccountsBalance
                .takeUntil(destroyNotifier)
                .subscribe {
                    liabilityAdapter.submitList(it.toList())
                }
        }
    }
}
