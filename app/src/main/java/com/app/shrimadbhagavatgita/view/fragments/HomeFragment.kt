package com.app.shrimadbhagavatgita.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.shrimadbhagavatgita.NetworkManager
import com.app.shrimadbhagavatgita.R
import com.app.shrimadbhagavatgita.databinding.FragmentHomeBinding
import com.app.shrimadbhagavatgita.datasource.api.room.SavedChapters
import com.app.shrimadbhagavatgita.models.ChaptersItem
import com.app.shrimadbhagavatgita.view.adapter.AdapterChapters
import com.app.shrimadbhagavatgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterChapters: AdapterChapters
    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setStatusBarColor()
        checkInternetConnectivity()

        binding.ivSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        // Inflate the layout for this fragment


        return binding.root
    }

    private fun showVerseOfTheDay() {
        val chapterNumber = (1..18).random()
        val verseNumber = (1..20).random()
        lifecycleScope.launch {
            viewModel.getAParticularVerse(chapterNumber, verseNumber).collect {
                for (i in it.translations) {
                    if (i.language == "english") {
                        binding.tvVerseOfTheDay.text = i.description
                        break

                    }
                }
            }
        }
    }

    fun onFavoriteClicked(chaptersItem: ChaptersItem) {

        lifecycleScope.launch {
            viewModel.putSavedChaptersSP(chaptersItem.chapter_number.toString(), chaptersItem.id)
            viewModel.getVerses(chaptersItem.chapter_number).collect {

                val verseList = arrayListOf<String>()
                for (currentVerse in it) {

                    for (verses in currentVerse.translations) {
                        if (verses.language == "english") {
                            verseList.add(verses.description)
                            break
                        }
                    }
                }
                val savedChapters = SavedChapters(
                    chapter_number = chaptersItem.chapter_number,
                    chapter_summary = chaptersItem.chapter_summary,
                    chapter_summary_hindi = chaptersItem.chapter_summary_hindi,
                    id = chaptersItem.id,
                    name = chaptersItem.name,
                    name_meaning = chaptersItem.name_meaning,
                    name_translated = chaptersItem.name_translated,
                    name_transliterated = chaptersItem.name_transliterated,
                    slug = chaptersItem.slug,
                    verses_count = chaptersItem.verses_count,
                    verses = verseList
                )

                viewModel.insertChapters(savedChapters)

            }
        }
    }

    fun onFavoriteFilledClicked(chaptersItem: ChaptersItem) {
        lifecycleScope.launch {
            viewModel.deleteSavedChaptersSP(chaptersItem.chapter_number.toString())
            viewModel.deleteChapter(chaptersItem.id)
        }
    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.shimmer.visibility = View.VISIBLE
                binding.rvChapters.visibility = View.VISIBLE
                binding.tvShowingMessage.visibility = View.GONE
                getAllChapters()
                showVerseOfTheDay()
            } else {
                binding.shimmer.visibility = View.GONE
                binding.rvChapters.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
            }

        }
    }

    private fun getAllChapters() {
        lifecycleScope.launch {
            viewModel.getAllChapters().collect { chapterList ->

                adapterChapters = AdapterChapters(
                    ::onChapterItemView,
                    ::onFavoriteClicked,
                    true,
                    ::onFavoriteFilledClicked,
                    viewModel
                )
                binding.rvChapters.adapter = adapterChapters
                adapterChapters.differ.submitList(chapterList)
                binding.shimmer.visibility = View.GONE
            }
        }
    }

    private fun onChapterItemView(chaptersItem: ChaptersItem) {
        val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItem.chapter_number)
        bundle.putString("chapterTitle", chaptersItem.name_translated)
        bundle.putString("chapterDescription", chaptersItem.chapter_summary)
        bundle.putInt("verseCount", chaptersItem.verses_count)
        findNavController().navigate(R.id.action_homeFragment_to_versesFragment, bundle)

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