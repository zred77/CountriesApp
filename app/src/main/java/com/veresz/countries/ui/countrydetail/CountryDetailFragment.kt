package com.veresz.countries.ui.countrydetail

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.transition.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.veresz.countries.R
import com.veresz.countries.databinding.FragmentCountrydetailBinding
import com.veresz.countries.model.Country
import com.veresz.countries.util.image.ImageSize
import com.veresz.countries.util.image.loadFlag
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_countrydetail.collapsingToolbarBackground
import kotlinx.android.synthetic.main.fragment_countrydetail.flag
import kotlinx.android.synthetic.main.fragment_countrydetail.toolbar
import kotlinx.android.synthetic.main.fragment_countrydetail.toolbar_layout

class CountryDetailFragment : DaggerFragment() {

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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val dominantColor = ColorDrawable(args.dominantColor)
        toolbar_layout.contentScrim = dominantColor
        toolbar_layout.setBackgroundColor(args.dominantColor)
        observeData()
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
        animSet.start()
    }
}
