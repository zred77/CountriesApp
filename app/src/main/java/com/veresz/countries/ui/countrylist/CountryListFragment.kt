package com.veresz.countries.ui.countrylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.veresz.countries.R
import com.veresz.countries.model.Country
import com.veresz.countries.ui.ViewState
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_countrylist.errorGroup
import kotlinx.android.synthetic.main.fragment_countrylist.fab
import kotlinx.android.synthetic.main.fragment_countrylist.recyclerView
import kotlinx.android.synthetic.main.fragment_countrylist.retry
import kotlinx.android.synthetic.main.fragment_countrylist.rootLayout
import kotlinx.android.synthetic.main.fragment_countrylist.swipeRefresh

class CountryListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter by lazy { CountryListAdapter() }
    private val viewModel by navGraphViewModels<CountryListViewModel>(R.id.filterable) { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_countrylist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeData()
        fab.isVisible = adapter.itemCount != 0
        setupRecyclerView()
        setWindowInsets()
        retry.setOnClickListener {
            viewModel.refresh()
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        fab.setOnClickListener {
            showFilter()
        }
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
            recyclerView.updatePadding(
                left = insets.systemWindowInsetLeft,
                top = insets.systemWindowInsetTop,
                right = insets.systemWindowInsetRight,
                bottom = insets.systemWindowInsetBottom
            )
            fab.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.systemWindowInsetLeft
                rightMargin = insets.systemWindowInsetRight + resources.getDimension(R.dimen.spacing_double).toInt()
                bottomMargin = insets.systemWindowInsetBottom + resources.getDimension(R.dimen.spacing_double).toInt()
            }
            insets
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.countries.observe(this, Observer {
            fab.isVisible = true
            showCountryList(it)
        })
        viewModel.viewState.observe(this, Observer {
            when (it) {
                is Error -> {
                    showErrorState()
                }
            }
            setLoadingState(it is ViewState.Loading)
        })
    }

    private fun setLoadingState(isLoading: Boolean) {
        swipeRefresh.isRefreshing = isLoading
    }

    private fun showCountryList(it: List<Country>) {
        adapter.submitList(it)
        recyclerView.isVisible = true
        errorGroup.isVisible = false
    }

    private fun showErrorState() {
        recyclerView.isVisible = false
        errorGroup.isVisible = true
    }

    private fun showFilter() {
        val direction = CountryListFragmentDirections.actionCountryListFragmentToFilterFragment()
        findNavController().navigate(direction)
    }
}
