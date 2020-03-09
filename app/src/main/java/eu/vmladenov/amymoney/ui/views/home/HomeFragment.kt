package eu.vmladenov.amymoney.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import kotlinx.android.synthetic.main.home_fragment.view.*

class HomeFragment : NavigationFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var tvNoData: TextView
    private lateinit var overviewContainer: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        tvNoData = view.tvNoData
        overviewContainer = view.overviewContainer
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, HomeViewModelFactory()).get(HomeViewModel::class.java)
        viewModel.isDataLoaded
            .takeUntil(destroyNotifier)
            .subscribe {
                tvNoData.visibility = if (it) View.GONE else View.VISIBLE
                overviewContainer.visibility = if (it) View.VISIBLE else View.GONE
            }
    }
}
