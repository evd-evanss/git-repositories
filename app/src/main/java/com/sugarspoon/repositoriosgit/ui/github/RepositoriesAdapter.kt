package com.sugarspoon.repositoriosgit.ui.github

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.repositoriosgit.databinding.ItemRepositoryBinding
import com.sugarspoon.repositoriosgit.utils.loadPicture

class RepositoriesAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemClicked: (RepositoryEntity) -> Unit = {}

    var list : MutableList<RepositoryEntity> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun updateList(data: List<RepositoryEntity>) {
        list.addAll(data)
    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return EventViewHolder(binding.root)
    }

     inner class EventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: RepositoryEntity) = ItemRepositoryBinding.bind(itemView).run {
            itemTitleTv.text = data.repositoryName
            itemAvatar.loadPicture(data.avatar)
            itemView.setOnClickListener {
                itemClicked.invoke(data)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EventViewHolder).bind(list[position])
    }
}