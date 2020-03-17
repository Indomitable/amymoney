package eu.vmladenov.amymoney.ui.views.transactions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import kotlinx.android.synthetic.main.fragment_transactions.view.*
import kotlinx.android.synthetic.main.fragment_transactions.view.balanceText
import java.text.NumberFormat
import java.util.*

class TransactionsFragment : NavigationFragment() {

    private lateinit var viewModel: TransactionsViewModel
    private lateinit var initialCounterAccount: Account
    private lateinit var balanceText: TextView
    private val formatter: NumberFormat = NumberFormat.getCurrencyInstance()
    private val transactionsAdapter: TransactionsAdapter = TransactionsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)

        val accountId = arguments?.let { TransactionsFragmentArgs.fromBundle(it).COUNTERACCOUNTID } ?: ""
        initialCounterAccount = repository.currentAccounts[accountId] ?: repository.currentAccounts.favoriteOrFirstUserAccount()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val transactionsFilter = TransactionsFilter(
            counterAccount = initialCounterAccount
        )
        formatter.currency = Currency.getInstance(transactionsFilter.counterAccount.currencyId)
        viewModel = ViewModelProvider(this, TransactionsViewModelFactory(transactionsFilter)).get(TransactionsViewModel::class.java)
            .also {
                bindToViewModel(it)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions, container, false)
        balanceText = view.balanceText
        initView(view)
        setEvents(view)
        return view
    }

    private fun bindToViewModel(viewModel: TransactionsViewModel) {
        viewModel.transactions
            .takeUntil(destroyNotifier)
            .subscribe {
                transactionsAdapter.update(viewModel.transactionsFilter.counterAccount, it, formatter)
            }
        viewModel.balance
            .takeUntil(destroyNotifier)
            .subscribe {
                balanceText.text = formatter.format(it.toDouble())
            }
    }

    private fun initView(view: View) {
        val transactionsView = view.transactionsList
        transactionsView.adapter = transactionsAdapter
        transactionsView.layoutManager = LinearLayoutManager(context)
        transactionsView.setHasFixedSize(true)
        transactionsView.addItemDecoration(TopBorderDecorator(requireContext()), 0)
    }

    private fun setEvents(view: View) {
        val transactionsView = view.transactionsList
        val manager = (transactionsView.layoutManager as LinearLayoutManager)

        transactionsView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val bottomThreshold = 3
            var loading = false
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (loading) {
                    return
                }
                val itemsCount = transactionsAdapter.itemCount

                val lastVisibleItem = manager.findLastVisibleItemPosition()
                if (itemsCount <= lastVisibleItem + bottomThreshold) {
                    loading = true
                    println(Log.d("Trans", "Loading, size: $itemsCount, lastVisible: $lastVisibleItem"))
                    recyclerView.post {
                        transactionsAdapter.loadMoreTransactions()
                        loading = false
                    }
                }
                viewModel.recalculateBalance(manager.findFirstVisibleItemPosition())
            }
        })
    }

    companion object {
        const val CounterAccountIdArg = "COUNTER_ACCOUNT_ID"

        @JvmStatic
        fun newInstance(counterAccountId: String) =
            TransactionsFragment().apply {
                arguments = TransactionsFragmentArgs(counterAccountId).toBundle()
            }
    }
}


interface TransactionClickHandler {
    fun handle(transactionId: String)
}

/**
 * Used to draw the top border of the first transaction
 */
class TopBorderDecorator(val context: Context) : RecyclerView.ItemDecoration() {
    private var mBounds = Rect()
    private val color: Int = ContextCompat.getColor(context, R.color.transaction_list_item_border_color)
    private val width: Float = context.resources.displayMetrics.density * 2f
    // TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, context.resources.displayMetrics)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()
        val childCount = parent.childCount
        if (childCount > 0) {
            val child = parent.getChildAt(0)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val top = mBounds.top.toFloat()
            if (top >= 0) {
                c.drawLine(0f, top, parent.width.toFloat(), 0f, Paint().also { p ->
                    p.color = color
                    p.strokeWidth = width
                })
            }
        }
        c.restore()
    }
}
