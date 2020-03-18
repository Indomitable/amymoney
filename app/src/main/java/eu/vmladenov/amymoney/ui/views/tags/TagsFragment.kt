package eu.vmladenov.amymoney.ui.views.tags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import eu.vmladenov.amymoney.R
import eu.vmladenov.amymoney.models.Tag
import eu.vmladenov.amymoney.ui.views.NavigationFragment
import kotlinx.android.synthetic.main.fragment_tags.view.*

class TagsFragment : NavigationFragment() {

    private val viewModel by viewModels<TagsViewModel>{ TagsViewModel.Factory() }
    private val tagsAdapter = TagsAdapter(object: TagClickHandler {
        override fun handle(tag: Tag) {

        }
    })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tags, container, false)

        viewModel.tags
            .takeUntil(destroyNotifier)
            .subscribe {
                tagsAdapter.updateItems(it)
            }

        with(view.tagsList) {
            adapter = tagsAdapter
        }

        return view
    }
}
