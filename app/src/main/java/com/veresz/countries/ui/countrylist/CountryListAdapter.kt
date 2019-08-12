package com.veresz.countries.ui.countrylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.veresz.countries.R
import com.veresz.countries.model.Country
import com.veresz.countries.util.image.ImageSize.small
import com.veresz.countries.util.image.loadFlag
import kotlinx.android.synthetic.main.item_country.view.countryName
import kotlinx.android.synthetic.main.item_country.view.flag

internal class CountryListAdapter : ListAdapter<Country, CountryViewHolder>(CountryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

internal class CountryViewHolder(
    private val view: View
) : ViewHolder(view) {

    fun bind(country: Country) {
        view.flag.loadFlag(country.alpha2Code, small, true)
        view.countryName.text = country.name
        view.setOnClickListener {
            view.flag.transitionName = "flag"
            val direction = CountryListFragmentDirections.actionCountryListFragmentToCountryDetailFragment(country)
            val extras = FragmentNavigatorExtras(
                view.flag to "flag"
            )
            view.findNavController().navigate(direction, extras)
        }
    }
}

internal class CountryDiffCallback : ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.alpha2Code == newItem.alpha2Code
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.name == newItem.name
    }
}
