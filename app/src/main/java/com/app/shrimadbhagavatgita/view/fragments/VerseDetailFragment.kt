package com.app.shrimadbhagavatgita.view.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.app.shrimadbhagavatgita.NetworkManager
import com.app.shrimadbhagavatgita.R
import com.app.shrimadbhagavatgita.databinding.FragmentVerseDetailBinding
import com.app.shrimadbhagavatgita.datasource.api.room.SavedVerses
import com.app.shrimadbhagavatgita.models.Commentary
import com.app.shrimadbhagavatgita.models.Translation
import com.app.shrimadbhagavatgita.models.VersesItem
import com.app.shrimadbhagavatgita.view.adapter.AdapterVerses
import com.app.shrimadbhagavatgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class VerseDetailFragment : Fragment() {
    private lateinit var binding: FragmentVerseDetailBinding

    private val viewModel: MainViewModel by viewModels()

    private var verseDetail = MutableLiveData<VersesItem>()
    private var chapterNum = 0
    private var verseNum = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerseDetailBinding.inflate(layoutInflater)
        setStatusBarColor()
        onReadMoreClicked()
        getAndSetChapterAndVerseNum()
        onSaveVerse()
        getData()

        return binding.root
    }

    private fun getData() {
        val bundle = arguments
        val showDataFromRoom = bundle?.getBoolean("showRoomData", false)

        if (showDataFromRoom == true) {
            binding.ivFavourite.visibility = View.GONE
            binding.ivFavouriteFilled.visibility = View.GONE
            getDataFromRoom()
        } else {
            checkInternetConnectivity()
        }
    }

    private fun getDataFromRoom() {
        viewModel.getParticularVerse(chapterNum, verseNum).observe(viewLifecycleOwner) { verse ->


            binding.apply {
                tvVerseText.text = verse.text
                tvtranliterationIfEnglish.text = verse.transliteration
                tvWordIfEnglish.text = verse.word_meanings

            }
            val englishTranslationList = arrayListOf<Translation>()

            for (i in verse.translations) {
                if (i.language == "english") {
                    englishTranslationList.add(i)

                }
            }
            //translation
            val englishTranslationListSize = englishTranslationList.size
            if (englishTranslationList.isNotEmpty()) {
                binding.tvAuthorName.text = englishTranslationList[0].author_name
                binding.tvText.text = englishTranslationList[0].description

                if (englishTranslationListSize == 1) {
                    binding.fabTranslationRight.visibility = View.GONE
                }

                var i = 0
                binding.fabTranslationRight.setOnClickListener {
                    if (i < englishTranslationListSize - 1) {
                        i++
                        binding.tvAuthorName.text = englishTranslationList[i].author_name
                        binding.tvText.text = englishTranslationList[i].description
                        binding.fabTranslationLeft.visibility = View.VISIBLE

                        if (i == englishTranslationListSize - 1) {
                            binding.fabTranslationRight.visibility = View.GONE

                        }
                    }
                }

                binding.fabTranslationLeft.setOnClickListener {
                    if (i > 0) {
                        i--
                        binding.tvAuthorName.text = englishTranslationList[i].author_name
                        binding.tvText.text = englishTranslationList[i].description
                        binding.fabTranslationRight.visibility = View.VISIBLE

                        if (i == 0) {
                            binding.fabTranslationLeft.visibility = View.GONE

                        }
                    }
                }
            }

            //comentry
            val englishCommentaryList = arrayListOf<Commentary>()

            for (i in verse.commentaries) {
                if (i.language == "english") {
                    englishCommentaryList.add(i)

                }
            }
            val englishCommentaryListSize = englishCommentaryList.size
            if (englishCommentaryList.isNotEmpty()) {
                binding.tvAuthorNameCommentry.text = englishCommentaryList[0].author_name
                binding.tvTextCommentry.text = englishCommentaryList[0].description

                if (englishCommentaryListSize == 1) {
                    binding.fabCommentriesRight.visibility = View.GONE
                }

                var i = 0
                binding.fabCommentriesRight.setOnClickListener {
                    if (i < englishCommentaryListSize - 1) {
                        i++
                        binding.tvAuthorNameCommentry.text =
                            englishCommentaryList[0].author_name
                        binding.tvTextCommentry.text = englishCommentaryList[0].description
                        binding.fabCommentriesLeft.visibility = View.VISIBLE

                        if (i == englishCommentaryListSize - 1) {
                            binding.fabCommentriesRight.visibility = View.GONE

                        }
                    }
                }

                binding.fabCommentriesLeft.setOnClickListener {
                    if (i > 0) {
                        i--
                        binding.tvAuthorNameCommentry.text =
                            englishCommentaryList[0].author_name
                        binding.tvTextCommentry.text = englishCommentaryList[0].description
                        binding.fabCommentriesRight.visibility = View.VISIBLE

                        if (i == 0) {
                            binding.fabCommentriesLeft.visibility = View.GONE

                        }
                    }
                }
            }


            binding.progressBar.visibility = View.GONE

            binding.tvVerseNumber.visibility = View.VISIBLE
            binding.tvVerseText.visibility = View.VISIBLE
            binding.tvtranliterationIfEnglish.visibility = View.VISIBLE
            binding.tvWordIfEnglish.visibility = View.VISIBLE
            binding.view.visibility = View.VISIBLE
            binding.tvTranslation.visibility = View.VISIBLE
            binding.clTranslation.visibility = View.VISIBLE
            binding.tvCommentries.visibility = View.VISIBLE
            binding.clCommentries.visibility = View.VISIBLE
            binding.backgroundImage.visibility = View.VISIBLE
            binding.ivFavourite.visibility = View.VISIBLE

        }
    }

    private fun onSaveVerse() {
        binding.apply {
            ivFavouriteFilled.setOnClickListener {
                binding.ivFavouriteFilled.visibility = View.GONE
                binding.ivFavourite.visibility = View.VISIBLE
                deleteVerse()
            }

            ivFavourite.setOnClickListener {
                binding.ivFavouriteFilled.visibility = View.VISIBLE
                binding.ivFavourite.visibility = View.GONE
                savingVerseDetails()
            }
        }
    }

    private fun deleteVerse() {
        lifecycleScope.launch {
            viewModel.deleteAParticularVerse(chapterNum,verseNum)
        }

    }

    private fun savingVerseDetails() {
        verseDetail.observe(viewLifecycleOwner) {

            val englishTranslationList = arrayListOf<Translation>()

            for (i in it.translations) {
                if (i.language == "english") {
                    englishTranslationList.add(i)

                }
            }

            //comentry
            val englishCommentaryList = arrayListOf<Commentary>()

            for (i in it.commentaries) {
                if (i.language == "english") {
                    englishCommentaryList.add(i)

                }
            }

            val savedVerses = SavedVerses(
                chapter_number = it.chapter_number,
                commentaries = englishCommentaryList,
                id = it.id,
                slug = it.slug,
                text = it.text,
                translations = englishTranslationList,
                transliteration = it.transliteration,
                verse_number = it.verse_number,
                word_meanings = it.word_meanings,

                )

            lifecycleScope.launch {
                viewModel.insertEnglishVerse(savedVerses)
            }

        }
    }


    //readmore
    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.tvSeeMore.setOnClickListener {
            if (!isExpanded) {
                binding.tvTextCommentry.maxLines = 100
                isExpanded = true
                binding.tvSeeMore.text = "Read Less.."
            } else {
                binding.tvTextCommentry.maxLines = 3
                isExpanded = false
                binding.tvSeeMore.text = "Read More.."
            }
        }
    }

    private fun getAndSetChapterAndVerseNum() {
        val bundle = arguments
        chapterNum = bundle?.getInt("chapterNum")!!
        verseNum = bundle?.getInt("verseNum")!!
        binding.apply {
            tvVerseNumber.text = "||$chapterNum.$verseNum||"

        }
    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.tvShowingMessage.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                getVerseDetail()
            } else {

                binding.tvShowingMessage.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

                binding.tvVerseNumber.visibility = View.GONE
                binding.tvVerseText.visibility = View.GONE
                binding.tvtranliterationIfEnglish.visibility = View.GONE
                binding.tvWordIfEnglish.visibility = View.GONE
                binding.view.visibility = View.GONE
                binding.tvTranslation.visibility = View.GONE
                binding.clTranslation.visibility = View.GONE
                binding.tvCommentries.visibility = View.GONE
                binding.clCommentries.visibility = View.GONE
                binding.backgroundImage.visibility = View.GONE
                binding.ivFavourite.visibility = View.GONE
            }

        }
    }

    private fun getVerseDetail() {
        lifecycleScope.launch {
            viewModel.getAParticularVerse(chapterNum, verseNum).collect { verse ->

                verseDetail.postValue(verse)

                binding.apply {
                    tvVerseText.text = verse.text
                    tvtranliterationIfEnglish.text = verse.transliteration
                    tvWordIfEnglish.text = verse.word_meanings
                }

                val englishTranslationList = arrayListOf<Translation>()

                for (i in verse.translations) {
                    if (i.language == "english") {
                        englishTranslationList.add(i)

                    }
                }
                //translation
                val englishTranslationListSize = englishTranslationList.size
                if (englishTranslationList.isNotEmpty()) {
                    binding.tvAuthorName.text = englishTranslationList[0].author_name
                    binding.tvText.text = englishTranslationList[0].description

                    if (englishTranslationListSize == 1) {
                        binding.fabTranslationRight.visibility = View.GONE
                    }

                    var i = 0
                    binding.fabTranslationRight.setOnClickListener {
                        if (i < englishTranslationListSize - 1) {
                            i++
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationLeft.visibility = View.VISIBLE

                            if (i == englishTranslationListSize - 1) {
                                binding.fabTranslationRight.visibility = View.GONE

                            }
                        }
                    }

                    binding.fabTranslationLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorName.text = englishTranslationList[i].author_name
                            binding.tvText.text = englishTranslationList[i].description
                            binding.fabTranslationRight.visibility = View.VISIBLE

                            if (i == 0) {
                                binding.fabTranslationLeft.visibility = View.GONE

                            }
                        }
                    }
                }

                //comentry
                val englishCommentaryList = arrayListOf<Commentary>()

                for (i in verse.commentaries) {
                    if (i.language == "english") {
                        englishCommentaryList.add(i)

                    }
                }
                val englishCommentaryListSize = englishCommentaryList.size
                if (englishCommentaryList.isNotEmpty()) {
                    binding.tvAuthorNameCommentry.text = englishCommentaryList[0].author_name
                    binding.tvTextCommentry.text = englishCommentaryList[0].description

                    if (englishCommentaryListSize == 1) {
                        binding.fabCommentriesRight.visibility = View.GONE
                    }

                    var i = 0
                    binding.fabCommentriesRight.setOnClickListener {
                        if (i < englishCommentaryListSize - 1) {
                            i++
                            binding.tvAuthorNameCommentry.text =
                                englishCommentaryList[0].author_name
                            binding.tvTextCommentry.text = englishCommentaryList[0].description
                            binding.fabCommentriesLeft.visibility = View.VISIBLE

                            if (i == englishCommentaryListSize - 1) {
                                binding.fabCommentriesRight.visibility = View.GONE

                            }
                        }
                    }

                    binding.fabCommentriesLeft.setOnClickListener {
                        if (i > 0) {
                            i--
                            binding.tvAuthorNameCommentry.text =
                                englishCommentaryList[0].author_name
                            binding.tvTextCommentry.text = englishCommentaryList[0].description
                            binding.fabCommentriesRight.visibility = View.VISIBLE

                            if (i == 0) {
                                binding.fabCommentriesLeft.visibility = View.GONE

                            }
                        }
                    }
                }

            }

            binding.progressBar.visibility = View.GONE

            binding.tvVerseNumber.visibility = View.VISIBLE
            binding.tvVerseText.visibility = View.VISIBLE
            binding.tvtranliterationIfEnglish.visibility = View.VISIBLE
            binding.tvWordIfEnglish.visibility = View.VISIBLE
            binding.view.visibility = View.VISIBLE
            binding.tvTranslation.visibility = View.VISIBLE
            binding.clTranslation.visibility = View.VISIBLE
            binding.tvCommentries.visibility = View.VISIBLE
            binding.clCommentries.visibility = View.VISIBLE
            binding.backgroundImage.visibility = View.VISIBLE
            binding.ivFavourite.visibility = View.VISIBLE
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


























