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
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.transition.doOnEnd
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.veresz.countries.R
import com.veresz.countries.model.Country
import com.veresz.countries.util.image.ImageSize
import com.veresz.countries.util.image.loadFlag
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_countrydetail.*

class CountryDetailFragment : DaggerFragment() {

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
        return inflater.inflate(R.layout.fragment_countrydetail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_layout.contentScrim = ColorDrawable(args.dominantColor)
        observeData()
    }

    fun getCountry(): Country {
        return args.country
    }

    private fun observeData() {
        viewModel.country.observe(this, Observer {
            flag.loadFlag(it.alpha2Code, ImageSize.small, true)
            collapsingToolbarBackground.loadFlag(it.alpha2Code, ImageSize.large)
        })
    }

    private fun setTransitions() {
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        val inSet = TransitionSet().apply {
            addTransition(transition)
            duration = 380
            interpolator = DecelerateInterpolator()
            doOnEnd {
                revealBigFlag()
            }
        }

        val outSet = TransitionSet().apply {
            addTransition(transition)
            duration = 380
            interpolator = AccelerateDecelerateInterpolator()
            startDelay = 200
        }

        sharedElementEnterTransition = inSet
        sharedElementReturnTransition = outSet
    }

    private fun revealBigFlag() {
        val center = Pair(flag.left + flag.width / 2, flag.top + flag.height / 2)
        val animSet = AnimatorSet()
        val reveal = ViewAnimationUtils.createCircularReveal(collapsingToolbarBackground, center.first,
                center.second, 0f, collapsingToolbarBackground.width.toFloat())
                .apply {
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
