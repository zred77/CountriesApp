package com.veresz.countries.ui.countrydetail

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.transition.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.veresz.countries.R
import com.veresz.countries.databinding.FragmentCountrydetailBinding
import com.veresz.countries.model.Country
import com.veresz.countries.util.image.ImageSize
import com.veresz.countries.util.image.loadFlag
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.content_scrolling.mapContainer
import kotlinx.android.synthetic.main.fragment_countrydetail.collapsingToolbarBackground
import kotlinx.android.synthetic.main.fragment_countrydetail.flag
import kotlinx.android.synthetic.main.fragment_countrydetail.rootLayout
import kotlinx.android.synthetic.main.fragment_countrydetail.toolbar
import kotlinx.android.synthetic.main.fragment_countrydetail.toolbar_layout

class CountryDetailFragment : DaggerFragment(), OnMapReadyCallback {

    private val anims = mutableSetOf<Animator>()
    private lateinit var binding: FragmentCountrydetailBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<CountryDetailViewModel> { viewModelFactory }
    private val args by navArgs<CountryDetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransitions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_countrydetail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowInsets()
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val dominantColor = ColorDrawable(args.dominantColor)
        toolbar_layout.contentScrim = dominantColor
        toolbar_layout.setBackgroundColor(args.dominantColor)
        observeData()
        if (viewModel.started == true) {
            setupMap()
        }
    }

    private fun setWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, insets ->
            rootLayout.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight,
                bottom = insets.systemWindowInsetBottom
            )
            insets
        }
    }

    override fun onStop() {
        anims.forEach { it.cancel() }
        super.onStop()
    }

    private fun setupMap() {
        if (!isAdded) return
        var mapFragment: SupportMapFragment? = childFragmentManager.findFragmentByTag("mapFragment") as? SupportMapFragment
        childFragmentManager.commit {
            if (mapFragment == null) {
                val googleMapOptions = GoogleMapOptions().liteMode(true)
                mapFragment = SupportMapFragment.newInstance(googleMapOptions)
                add(R.id.mapContainer, mapFragment!!, "mapFragment")
            }
        }
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        val country = viewModel.country.value!!
        map.uiSettings.isMapToolbarEnabled = false
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(country.latlng[0], country.latlng[1]), 4f))
        anims.add(ValueAnimator.ofFloat(0f, 1f).apply {
            doOnStart {
                mapContainer.isVisible = true
            }
            addUpdateListener {
                mapContainer.alpha = it.animatedValue as Float
            }
            start()
        })
        map.setOnMapClickListener {
            openMaps(country.name)
        }
    }

    private fun openMaps(countryName: String) {
        val uri = Uri.parse("geo:0,0?q=$countryName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    fun getCountry(): Country {
        return args.country
    }

    private fun observeData() {
        viewModel.country.observe(this, Observer {
            binding.country = it
            flag.loadFlag(it.alpha2Code, ImageSize.small, true)
            collapsingToolbarBackground.loadFlag(it.alpha2Code, ImageSize.large)
        })
    }

    private fun setTransitions() {
        viewModel.started = true
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        val inSet = TransitionSet().apply {
            addTransition(transition)
            duration = 380
            interpolator = AccelerateInterpolator()
            doOnEnd {
                revealBigFlag()
            }
        }

        sharedElementEnterTransition = inSet
        enterTransition = TransitionInflater.from(context).inflateTransition(R.transition.detail_in)
        returnTransition = TransitionInflater.from(context).inflateTransition(R.transition.detail_out)
    }

    private fun revealBigFlag() {
        val center = Pair(flag.left + flag.width / 2, flag.top + flag.height / 2)
        val animSet = AnimatorSet()
        val reveal = ViewAnimationUtils
            .createCircularReveal(
                collapsingToolbarBackground, center.first,
                center.second, 0f, collapsingToolbarBackground.width.toFloat()
            )
            .apply {
                doOnEnd {
                    setupMap()
                }
                interpolator = AccelerateInterpolator()
                duration = 400
            }

        val hide = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 100
            addUpdateListener {
                flag.alpha = it.animatedValue as Float
            }
        }
        animSet.playTogether(reveal, hide)
        collapsingToolbarBackground.visibility = View.VISIBLE
        anims.add(animSet)
        animSet.start()
    }
}
