package com.abdallah.sarrawi.mymsgs.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.databinding.FragmentSplashBinding
import java.text.SimpleDateFormat
import java.util.*


class SplashFragment : Fragment() {

    private lateinit var _binding : FragmentSplashBinding
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSplash:TextView =view.findViewById(R.id.mmm)
        val imgSplash:ImageView =view.findViewById(R.id.imageView)
        val currentDate = Calendar.getInstance().time

        // قم بتحديد تنسيق السنة
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        // احصل على السنة من التاريخ الحالي
        val currentYear = yearFormat.format(currentDate)

        // قم بعرض السنة في TextView
        tvSplash.text = "مـسـجـاتـي \n$currentYear"
//        val slideAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim)

//        tvSplash.animate().translationXBy(-1000f).duration=3000
//        tvSplash.animate().alpha(0.5f)
//        tvSplash.animate().translationYBy(1000f).rotation(360f).duration=3000
//        imgSplash.animate().rotation(360f).duration=3000
//        tvSplash.startAnimation(slideAnimation)

        var uptodown = AnimationUtils.loadAnimation(requireContext(), R.anim.uptodown)
        var downtoup = AnimationUtils.loadAnimation(requireContext(), R.anim.downtoup)

        tvSplash.animate(). rotation(360f).duration=3000
        imgSplash.animate().rotation(360f).duration=3000

        tvSplash.setAnimation(downtoup)
        imgSplash.setAnimation(uptodown)

        Handler(Looper.myLooper()!!).postDelayed({
//            val direction = SplashFragmentDirections.actionSplashFragmentToFirsFragment()
//            findNavController().navigate(direction)

            findNavController()
                .navigate(R.id.action_splashFragment_to_firsFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashFragment,
                            true).build()
                )

        },5000)

    }


}