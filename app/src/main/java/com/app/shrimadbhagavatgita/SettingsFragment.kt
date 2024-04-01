package com.app.shrimadbhagavatgita

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.shrimadbhagavatgita.databinding.FragmentHomeBinding
import com.app.shrimadbhagavatgita.databinding.FragmentSettingsBinding
import com.app.shrimadbhagavatgita.view.adapter.AdapterChapters
import com.app.shrimadbhagavatgita.viewmodel.MainViewModel


class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        setStatusBarColor()

        binding.llSavedChapters.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_savedChaptersFragment)
        }
        binding.llSavedVerses.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_savedVersesFragment)
        }
        // Inflate the layout for this fragment

        binding.llShareApp.setOnClickListener {
            shareApp()
        }

        binding.llRateApp.setOnClickListener {
            rateApp()
        }

        binding.llMoreApps.setOnClickListener {
            moreApps()
        }

        binding.llPrivacyPolicy.setOnClickListener {
            privacyPolicy()
        }


        return binding.root
    }


    private fun moreApps() {
        val appPackageName = requireContext().packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7691527306445378965")))
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7691527306445378965")))
        }
    }






    private fun privacyPolicy() {
        val privacyPolicyUrl = getString(R.string.privacy_policy)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
        startActivity(intent)
    }


    private fun shareApp() {
        val appPackageName = requireContext().packageName
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message) + " " +"https://play.google.com/store/apps/details?id=$appPackageName")
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_title)))
    }

    private fun rateApp() {
        try {
            val appPackageName = requireContext().packageName
            val playStoreUri = Uri.parse("market://details?id=$appPackageName")
            val rateIntent = Intent(Intent.ACTION_VIEW, playStoreUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            // If Google Play Store app is not installed, open the app page in a web browser
            val packageName = requireContext().packageName
            val webPlayStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val rateIntent = Intent(Intent.ACTION_VIEW, webPlayStoreUri)
            startActivity(rateIntent)
        }
    }



    //status bar color
    private fun setStatusBarColor() {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.white)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}