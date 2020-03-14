package eu.vmladenov.amymoney.ui.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.infrastructure.ProgressReporter
import eu.vmladenov.amymoney.ui.views.DisposableDialogFragment
import kotlinx.android.synthetic.main.fragment_load_progress.view.*

class LoadProgressFragment(private val reporter: ProgressReporter) : DisposableDialogFragment() {
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

    suspend fun listen() {
        val dialog = this
        for (message in reporter.messages) {
            progressView.text = message
        }
        dialog.dismiss()
    }
}
