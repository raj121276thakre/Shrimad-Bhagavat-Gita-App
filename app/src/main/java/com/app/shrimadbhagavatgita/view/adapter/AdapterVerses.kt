package com.app.shrimadbhagavatgita.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.shrimadbhagavatgita.databinding.ItemViewVersesBinding

class AdapterVerses(val onVerseItemViewClicked: (String, Int) -> Unit, val onClick: Boolean) : RecyclerView.Adapter<AdapterVerses.VerseViewHolder>() {
    class VerseViewHolder(val binding: ItemViewVersesBinding) :
        RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        return AdapterVerses.VerseViewHolder(
            ItemViewVersesBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        val verse = differ.currentList[position]
        holder.binding.apply {
            tvVerseNumber.text ="Verse ${position + 1}"
            tvVerse.text = verse

        }
        if (onClick) {
            holder.binding.ivNext.visibility =View.VISIBLE

        } else{
            holder.binding.ivNext.visibility =View.GONE
        }
        holder.binding.ll.setOnClickListener {
            if (onClick) {

                onVerseItemViewClicked(verse, position + 1)
            }

        }
    }
}

















