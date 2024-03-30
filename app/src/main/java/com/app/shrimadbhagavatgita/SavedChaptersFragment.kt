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
import com.app.shrimadbhagavatgita.databinding.FragmentSavedChaptersBinding
import com.app.shrimadbhagavatgita.models.ChaptersItem
import com.app.shrimadbhagavatgita.view.adapter.AdapterChapters
import com.app.shrimadbhagavatgita.viewmodel.MainViewModel

class SavedChaptersFragment : Fragment() {
    private lateinit var binding: FragmentSavedChaptersBinding
    private lateinit var adapterChapters: AdapterChapters
    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedChaptersBinding.inflate(layoutInflater)
        setStatusBarColor()

        // Inflate the layout for this fragment
        getSavedChapters()


        return binding.root
    }

    private fun getSavedChapters() {

            viewModel.getSavedChapters().observe(viewLifecycleOwner) {
                val chapterList = arrayListOf<ChaptersItem>()
                for (i in it) {
                    val chaptersItem = ChaptersItem(
                        i.chapter_number,
                        i.chapter_summary,
                        i.chapter_summary_hindi,
                        i.id,
                        i.name,
                        i.name_meaning,
                        i.name_translated,
                        i.name_transliterated,
                        i.slug,
                        i.verses_count,
                    )

                    chapterList.add(chaptersItem)
                }

                if (chapterList.isEmpty()){
                    binding.shimmer.visibility = View.GONE
                    binding.rvChapters.visibility = View.GONE
                    binding.tvShowingMessage.visibility = View.VISIBLE
                }

                adapterChapters = AdapterChapters(
                    ::onChapterItemVClicked,
                    ::onFavoriteClicked,
                    false,
                    ::onFavoriteFilledClicked,
                    viewModel
                )
                binding.rvChapters.adapter = adapterChapters
                adapterChapters.differ.submitList(chapterList)

                binding.shimmer.visibility = View.GONE
                binding.rvChapters.visibility = View.VISIBLE
            }

    }
    fun onFavoriteFilledClicked(chaptersItem: ChaptersItem) {

    }


    fun onChapterItemVClicked(chaptersItem: ChaptersItem){
        val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItem.chapter_number)
        bundle.putBoolean("showRoomData", true)
        findNavController().navigate(R.id.action_savedChaptersFragment_to_versesFragment, bundle)

    }

    fun onFavoriteClicked(chaptersItem: ChaptersItem){

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