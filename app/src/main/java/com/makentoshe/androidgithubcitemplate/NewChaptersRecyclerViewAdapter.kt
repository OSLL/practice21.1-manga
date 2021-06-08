package com.makentoshe.androidgithubcitemplate

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request


class NewChaptersRecyclerViewAdapter(private val data: List<Manga>,
                                     private val lifecycleCoroutineScope: LifecycleCoroutineScope,
                                     private val client: OkHttpClient) :
    RecyclerView.Adapter<NewChaptersRecyclerViewAdapter.TopViewHolder>(){

    private val cache  = HashMap<String, Bitmap>()

    //class that handle single element of recycler view
    class TopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var mangaPreviewImage: ImageView = itemView.findViewById(R.id.mangaPreviewImage)
        var mangaTitle: TextView = itemView.findViewById(R.id.mangaTitle)
        var mangaChapter: TextView = itemView.findViewById(R.id.mangaChapter)
        var mangaPublishTime: TextView = itemView.findViewById(R.id.mangaChapterPublishTime)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TopViewHolder, position: Int) {
        holder.mangaTitle.text = data[position].title
        holder.mangaChapter.text = data[position].genre

        getMangaImage(data[position].imageUrl){ bitmap ->
            holder.mangaPreviewImage.setImageBitmap(bitmap)
        }
        holder.mangaPublishTime.text = "Some minutes age" // Add special structure for last chapter e.t.c.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.new_chapters_recyclerview_element,
                parent, false)

        return TopViewHolder(itemView)
    }

    private fun getMangaImage(imageUrl: String, after: (image: Bitmap) -> Unit) {
        if (cache.containsKey(imageUrl)) {
            return after.invoke(cache[imageUrl]!!)
        }

        lifecycleCoroutineScope.launch(Dispatchers.IO) {
            val response =
                client.newCall(Request.Builder().url(imageUrl).build()).execute()
            if (response.isSuccessful) {
                val bitmap = BitmapFactory.decodeStream(response.body?.byteStream())
                cache[imageUrl] = bitmap
                lifecycleCoroutineScope.launch(Dispatchers.Main){
                    after.invoke(bitmap)
                }
            } else {
                println(response.message)
            }
        }
    }

}