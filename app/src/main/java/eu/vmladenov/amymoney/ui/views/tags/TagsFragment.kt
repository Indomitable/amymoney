package eu.vmladenov.amymoney.ui.views.tags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.ui.views.NavigationFragment

class TagsFragment : NavigationFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tags, container, false)
    }

}
