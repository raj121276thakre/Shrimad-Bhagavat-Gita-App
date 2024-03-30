package com.app.shrimadbhagavatgita.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.shrimadbhagavatgita.databinding.ItemViewChaptersBinding
import com.app.shrimadbhagavatgita.models.ChaptersItem
import com.app.shrimadbhagavatgita.viewmodel.MainViewModel

class AdapterChapters(
    val onChapterItemView: (ChaptersItem) -> Unit,
    val onFavoriteClicked: (ChaptersItem) -> Unit,
    var showSaveButton: Boolean,
    val onFavoriteFilledClicked: (ChaptersItem) -> Unit,
    val viewModel: MainViewModel
) : RecyclerView.Adapter<AdapterChapters.ChaptersViewHolder>() {
    class ChaptersViewHolder(val binding: ItemViewChaptersBinding) : ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<ChaptersItem>() {
        override fun areItemsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        return ChaptersViewHolder(
            ItemViewChaptersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
        val chapter = differ.currentList[position]

        val keys = viewModel.getAllSavedChaptersKey()

        if (!showSaveButton) {
            holder.binding.apply {
                ivFavourite.visibility = View.GONE
                ivFavouriteFilled.visibility = View.GONE
            }
        }else{

            if (keys.contains(chapter.chapter_number.toString())){
                holder.binding.apply {
                    ivFavourite.visibility = View.GONE
                    ivFavouriteFilled.visibility = View.VISIBLE
                }
            }
            else{
                holder.binding.apply {
                    ivFavourite.visibility = View.VISIBLE
                    ivFavouriteFilled.visibility = View.GONE
                }
            }

        }


        holder.binding.apply {
            tvChapterNumber.text = "Chapter ${chapter.chapter_number}"
            tvChapterTitle.text = chapter.name_translated
            tvChapterDescription.text = chapter.chapter_summary
            tvVerseCount.text = chapter.verses_count.toString()
        }

        holder.binding.ll.setOnClickListener {
            onChapterItemView(chapter)
        }
        holder.binding.apply {
            ivFavourite.setOnClickListener {
                ivFavouriteFilled.visibility = View.VISIBLE
                ivFavourite.visibility = View.GONE
                onFavoriteClicked(chapter)
            }
            ivFavouriteFilled.setOnClickListener {
                ivFavourite.visibility = View.VISIBLE
                ivFavouriteFilled.visibility = View.GONE
                onFavoriteFilledClicked(chapter)

            }
        }


    }
}














