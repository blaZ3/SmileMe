package me.tellvivk.smileme.app.screens.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_item_image.view.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.helpers.imageHelper.ImageHelper
import me.tellvivk.smileme.helpers.imageHelper.ImageHelperI
import java.io.File
import java.util.*

class HomeImagesAdapter(
    var items: List<Image>,
    private val context: Context,
    private val imageInterface: HomeImagesAdapterInterface,
    private val imageHelper: ImageHelperI
) : RecyclerView.Adapter<HomeImagesAdapter.HomeImagesViewHolder>() {

    class HomeImagesViewHolder(private val view: View,
                               private val adapterInterface: HomeImagesAdapterInterface,
                               private val imageHelper: ImageHelperI) :
        RecyclerView.ViewHolder(view) {
        fun onBind(item: Image) {

            view.txtListImageTitle.text = item.title

            if (!item.filePath.isNullOrEmpty()){
                imageHelper.loadFromFile(context = view.context,
                    file = File(item.filePath), iv = view.imgListImage)
            }else {
                item.imgUrl?.let {
//                    val url = "$it&cacheBust=${UUID.randomUUID().hashCode()}"
                    imageHelper.loadFromUrl(context = view.context, url = it,
                        iv = view.imgListImage)
                }
            }
            view.setOnClickListener { adapterInterface.onImageClicked(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeImagesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_image, parent, false)
        return HomeImagesViewHolder(view, imageInterface, imageHelper)
    }

    override fun onBindViewHolder(holder: HomeImagesViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    interface HomeImagesAdapterInterface {
        fun onImageClicked(image: Image)
    }

}