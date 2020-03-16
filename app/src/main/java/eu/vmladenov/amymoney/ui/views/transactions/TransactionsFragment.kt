package eu.vmladenov.amymoney.ui.views.transactions

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
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
import eu.vmladenov.amymoney.models.Transaction
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import kotlinx.android.synthetic.main.fragment_transactions.*
import kotlinx.android.synthetic.main.fragment_transactions.view.*
import kotlinx.android.synthetic.main.fragment_transactions.view.balanceText
import java.text.NumberFormat

class TransactionsFragment : NavigationFragment() {

    private lateinit var viewModel: TransactionsViewModel
    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var initialCounterAccountId: String
    private lateinit var balanceText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)

        transactionsAdapter = TransactionsAdapter(repository)
        initialCounterAccountId = arguments?.let { TransactionsFragmentArgs.fromBundle(it).COUNTERACCOUNTID } ?: ""
        if (initialCounterAccountId.isEmpty()) {
            initialCounterAccountId = repository.currentAccounts.favoriteOrFirstUserAccount().id
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val transactionsFilter = TransactionsFilter(
            counterAccountId = initialCounterAccountId
        )

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
        return view
    }

    private fun bindToViewModel(viewModel: TransactionsViewModel) {
        viewModel.transactions
            .takeUntil(destroyNotifier)
            .subscribe {
                transactionsAdapter.setSource(viewModel.transactionsFilter.counterAccountId, it)
            }
        viewModel.balance
            .takeUntil(destroyNotifier)
            .subscribe {
                balanceText.text = it
            }
    }

    private fun initView(view: View) {
        val transactionsView = view.transactionsList
        transactionsView.adapter = transactionsAdapter
        transactionsView.layoutManager = LinearLayoutManager(context)
        transactionsView.addItemDecoration(TopBorderDecorator(requireContext()), 0)
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
    fun handle(transaction: Transaction)
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
