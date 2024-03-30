package com.app.shrimadbhagavatgita

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



        return binding.root
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