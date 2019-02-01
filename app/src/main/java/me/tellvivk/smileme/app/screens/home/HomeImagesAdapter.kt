package me.tellvivk.smileme.app.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.model.Image

class HomeImagesAdapter(var items: List<Image>,
                        private val context: Context): RecyclerView.Adapter<HomeImagesAdapter.HomeImagesViewHolder>() {

    class HomeImagesViewHolder(v: View): RecyclerView.ViewHolder(v){
        fun onBind(item: Image){
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeImagesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_image, parent, false)
        return HomeImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeImagesViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}