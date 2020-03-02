package eu.vmladenov.amymoney.ui.views.accounts

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Account

class AccountsFragment : Fragment() {


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
        val recyclerView = view.findViewById<RecyclerView>(R.id.accountsList)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = AccountsAdapter(object : AccountClickHandler {
                override fun handle(account: Account) {

                }
            })
        }


        return view
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
