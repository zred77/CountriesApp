package com.veresz.countries.ui.filter

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navGraphViewModels
import com.google.android.material.chip.Chip
import com.veresz.countries.R
import com.veresz.countries.ui.countrylist.CountryListViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by navGraphViewModels<CountryListViewModel>(R.id.filterable) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets()
        observeData()
        reset.setOnClickListener {
            onResetClicked()
        }
    }

    private fun setTransitions() {
        val slideBottom = TransitionInflater.from(context).inflateTransition(android.R.transition.slide_bottom)
        enterTransition = slideBottom
        returnTransition = slideBottom
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(filterRoot) { view, insets ->
            filterRoot.updatePadding(
                    top = insets.systemWindowInsetTop,
                    bottom = insets.systemWindowInsetBottom
            )
            insets
        }
    }

    private fun onResetClicked() {
        viewModel.resetFilters()
        chipGroup.children
                .filterIsInstance<Chip>()
                .forEach { chip ->
                    chip.isChecked = false
                }
    }

    private fun observeData() {
        viewModel.regionFilters.observe(this, Observer {
            addFilters(it)
        })
        viewModel.countries.observe(this, Observer {
            title.text = getString(R.string.filter_count, it.size.toString())
        })
    }

    private fun addFilters(filters: List<String>) {
        chipGroup.removeAllViews()
        val selectedFilters = viewModel.selectedFilters
        filters.forEach { item ->
            Chip(context).apply {
                isCheckable = true
                isChecked = selectedFilters.contains(item)
                text = item
                setOnClickListener {
                    viewModel.onFilterChecked(item, isChecked)
                }
                setOnCheckedChangeListener { buttonView, isChecked ->
                    viewModel.onFilterChecked(item, isChecked)
                }
                chipGroup.addView(this)
            }
        }
    }
}
