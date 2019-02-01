package me.tellvivk.smileme.app.screens.home.adapter

import androidx.recyclerview.widget.DiffUtil
import me.tellvivk.smileme.app.model.Image

class ImagesListDiffCallback(private val oldItems: List<Image>, private val newItems: List<Image>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}