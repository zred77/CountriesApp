package com.veresz.countries.ui.countrylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.veresz.countries.R
import com.veresz.countries.model.Country
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_countrylist.*

class CountryListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter by lazy { CountryListAdapter() }
    private val viewModel by viewModels<CountryListViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_countrylist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        retry.setOnClickListener {
            viewModel.refresh()
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.countries().observe(this, Observer {
            when {
                it.isSuccess -> {
                    showCountryList(it)
                }
                it.isFailure -> {
                    showErrorState()
                }
            }
        })
        viewModel.loadingState().observe(this, Observer {
            setLoadingState(it)
        })
    }

    private fun setLoadingState(isLoading: Boolean) {
        swipeRefresh.isRefreshing = isLoading
    }

    private fun showCountryList(it: Result<List<Country>>) {
        adapter.submitList(it.getOrElse { listOf() })
        recyclerView.isVisible = true
        errorGroup.isVisible = false
    }

    private fun showErrorState() {
        recyclerView.isVisible = false
        errorGroup.isVisible = true
    }
}
