package eu.vmladenov.amymoney.ui.views.transactions.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import eu.vmladenov.amymoney.R

/**
 * A simple [Fragment] subclass.
 */
class TransactionEditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.transaction_edit, container, false)
    }

}
