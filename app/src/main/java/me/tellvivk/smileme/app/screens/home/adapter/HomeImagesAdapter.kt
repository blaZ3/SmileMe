package me.tellvivk.smileme.app.screens.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_image.view.*
import me.tellvivk.smileme.R
import me.tellvivk.smileme.app.model.Image
import java.util.*

class HomeImagesAdapter(
    var items: List<Image>,
    private val context: Context,
    private val imageInterface: HomeImagesAdapterInterface
) : RecyclerView.Adapter<HomeImagesAdapter.HomeImagesViewHolder>() {

    class HomeImagesViewHolder(private val view: View,
                               private val adapterInterface: HomeImagesAdapterInterface) :
        RecyclerView.ViewHolder(view) {
        fun onBind(item: Image) {

            view.txtListImageTitle.text = item.title

            if (item.bitmap != null){
                view.imgListImage.setImageBitmap(item.bitmap)
            }else {
                item.imgUrl?.let {
                    val url = "$it&cacheBust=${UUID.randomUUID().hashCode()}"
                    Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.place_holder)
                        .into(view.imgListImage)
                }
            }
            view.setOnClickListener { adapterInterface.onImageClicked(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeImagesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_image, parent, false)
        return HomeImagesViewHolder(view, imageInterface)
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