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
import com.app.shrimadbhagavatgita.databinding.FragmentSavedVersesBinding
import com.app.shrimadbhagavatgita.databinding.FragmentSettingsBinding
import com.app.shrimadbhagavatgita.datasource.api.room.SavedVerses
import com.app.shrimadbhagavatgita.models.ChaptersItem
import com.app.shrimadbhagavatgita.models.VersesItem
import com.app.shrimadbhagavatgita.view.adapter.AdapterChapters
import com.app.shrimadbhagavatgita.view.adapter.AdapterSavedVerses
import com.app.shrimadbhagavatgita.view.adapter.AdapterVerses
import com.app.shrimadbhagavatgita.viewmodel.MainViewModel


class SavedVersesFragment : Fragment() {
    private lateinit var binding: FragmentSavedVersesBinding
    private lateinit var adapterSavedVerses: AdapterSavedVerses
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedVersesBinding.inflate(layoutInflater)
        setStatusBarColor()
        // Inflate the layout for this fragment

        getVersesFromRoom()


        return binding.root
    }

    private fun getVersesFromRoom() {
        viewModel.getAllEnglishVerse().observe(viewLifecycleOwner) {

            adapterSavedVerses = AdapterSavedVerses(::onVerseItemViewClicked)
            binding.rvVerses.adapter = adapterSavedVerses
            adapterSavedVerses.differ.submitList(it)
            binding.shimmer.visibility = View.GONE

            binding.rvVerses.visibility = View.VISIBLE
        }

    }

    fun onVerseItemViewClicked(verses : SavedVerses){
        val bundle = Bundle()
        bundle.putInt("chapterNum", verses.chapter_number)
        bundle.putInt("verseNum", verses.verse_number)
        bundle.putBoolean("showRoomData", true)
        findNavController().navigate(R.id.action_savedVersesFragment_to_verseDetailFragment, bundle)
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