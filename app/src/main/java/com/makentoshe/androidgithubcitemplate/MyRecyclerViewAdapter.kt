package com.makentoshe.androidgithubcitemplate

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.CopyOnWriteArrayList


class MyRecyclerViewAdapter(private val data: List<Manga>,
                            private val screenSize: ImageSize,
                            private val  lifecycleCoroutineScope: LifecycleCoroutineScope) :
RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>(){

    private val mangaImages: CopyOnWriteArrayList<Bitmap?> = generateEmptyCaches()

    //class that handle single element of recycler view
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mangaPreviewImage: ImageView = itemView.findViewById(R.id.mangaPreviewImage)
        var mangaTitle: TextView = itemView.findViewById(R.id.mangaTitle)
        var mangaGenre: TextView = itemView.findViewById(R.id.mangaGenre)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.mangaTitle.text = data[position].title
        holder.mangaGenre.text = data[position].genre
        if(mangaImages[position] == null){
//            mangaImages[position] = getNewImage(data[position].imge, screenSize)
        }
        holder.mangaPreviewImage.setImageBitmap(mangaImages[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_item,
            parent, false)

        return MyViewHolder(itemView)
    }

    private fun getNewImage(image: Bitmap, screenSize: ImageSize, imagesOnScreen: Double = 2.5) : Bitmap{
        val newImageWidth = (screenSize.width.toDouble() / imagesOnScreen - screenSize.width.toDouble() * 0.05).toInt()
        val resizer = ImageResizer(image, newImageWidth) // 0 passed to save image ratio

        return resizer.getResizedImage()
    }

    private fun generateEmptyCaches(): CopyOnWriteArrayList<Bitmap?> {
        val result = CopyOnWriteArrayList<Bitmap?>()
        for(i in data){
            result.add(null)
        }
        return result
    }

}