package eu.vmladenov.amymoney.ui.views.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.ProgressReporter
import eu.vmladenov.amymoney.ui.views.DisposableDialogFragment
import kotlinx.android.synthetic.main.fragment_load_progress.view.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LoadProgressFragment() : DisposableDialogFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var progressView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_load_progress, container, false)
        progressView = view.load_progress_info
        return view
    }


    var reporter: ProgressReporter? = null
        set(value) {
            if (value != null) {
                value.messages
                    .takeUntil(destroyNotifier)
                    .doFinally {
                        this.dismiss()
                    }
                    .subscribe { message ->
                        progressView.text = message
                    }
            }
            field = value
        }
}
