package com.app.shrimadbhagavatgita.view.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.shrimadbhagavatgita.NetworkManager
import com.app.shrimadbhagavatgita.R
import com.app.shrimadbhagavatgita.databinding.FragmentVersesBinding
import com.app.shrimadbhagavatgita.view.adapter.AdapterVerses
import com.app.shrimadbhagavatgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class VersesFragment : Fragment() {


    private lateinit var binding: FragmentVersesBinding
    private lateinit var adapterVerses: AdapterVerses
    private val viewModel: MainViewModel by viewModels()
    private var chapterNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVersesBinding.inflate(layoutInflater)
        setStatusBarColor()

        // Inflate the layout for this fragment
        onReadMoreClicked()
        getAndSetChapterDetail()

        getData()


        return binding.root
    }

    private fun getData() {
        val bundle = arguments
        val showDataFromRoom = bundle?.getBoolean("showRoomData", false)

        if (showDataFromRoom == true){
            getDataFromRoom()
        }
        else{
            checkInternetConnectivity()
        }
    }

    private fun getDataFromRoom() {
        viewModel.getAParticularChapter(chapterNumber).observe(viewLifecycleOwner) {

            binding.apply {
                tvChaptersNumber.text = "Chapter ${it.chapter_number}"
                tvChaptersTitle.text = it.name_translated
                tvChaptersDescription.text = it.chapter_summary
                tvVerseCount.text = it.verses_count.toString()

                showListInAdapter(it.verses, false)
            }
        }

    }

    //readmore
    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.tvSeeMore.setOnClickListener {
            if (!isExpanded) {
                binding.tvChaptersDescription.maxLines = 50
                isExpanded = true
                binding.tvSeeMore.text = "Read Less.."
            } else {
                binding.tvChaptersDescription.maxLines = 4
                isExpanded = false
                binding.tvSeeMore.text = "Read More.."
            }
        }
    }

    private fun getAndSetChapterDetail() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber")!!
        binding.apply {
            tvChaptersNumber.text = "Chapter ${bundle?.getInt("chapterNumber")}"
            tvChaptersTitle.text = bundle?.getString("chapterTitle")
            tvChaptersDescription.text = bundle?.getString("chapterDescription")
            tvVerseCount.text = bundle?.getInt("verseCount").toString()
        }
    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.shimmer.visibility = View.VISIBLE
                binding.rvVerses.visibility = View.VISIBLE
                binding.tvShowingMessage.visibility = View.GONE
                getAllVerses()
            } else {
                binding.shimmer.visibility = View.GONE
                binding.rvVerses.visibility = View.GONE
                binding.tvShowingMessage.visibility = View.VISIBLE
            }

        }
    }

    private fun getAllVerses() {
        lifecycleScope.launch {
            viewModel.getVerses(chapterNumber).collect {


                val verseList = arrayListOf<String>()
                for (currentVerse in it) {

                    for (verses in currentVerse.translations) {
                        if (verses.language == "english") {
                            verseList.add(verses.description)
                            break
                        }
                    }
                }
                showListInAdapter(verseList, true)

            }
        }
    }

    private fun showListInAdapter(verseList: List<String>, onClick : Boolean) {
        adapterVerses = AdapterVerses(::onVerseItemViewClicked, onClick)
        binding.rvVerses.adapter = adapterVerses
        adapterVerses.differ.submitList(verseList)
        binding.shimmer.visibility = View.GONE
    }

    private fun onVerseItemViewClicked(verse: String, verseNumber: Int) {
        val bundle = Bundle()
        bundle.putInt("chapterNum", chapterNumber)
        bundle.putInt("verseNum", verseNumber)
        findNavController().navigate(R.id.action_versesFragment_to_verseDetailFragment, bundle)
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










