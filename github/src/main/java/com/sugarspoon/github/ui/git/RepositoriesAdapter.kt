package com.sugarspoon.github.ui.git

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.core.databinding.ItemRepositoryBinding
import com.sugarspoon.core.utils.loadPicture
import com.sugarspoon.github.model.local.RepositoryEntity

class RepositoriesAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemClicked: (RepositoryEntity) -> Unit = {}

    private val list: MutableList<RepositoryEntity> = mutableListOf()

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

    fun updateData(data: List<RepositoryEntity>) {
        val diffCallback = RepositoryEntityDiffCallback(this.list, data)
        val diffUtilResult = DiffUtil.calculateDiff(diffCallback)
        this.list.clear()
        this.list.addAll(data)
        diffUtilResult.dispatchUpdatesTo(this@RepositoriesAdapter)
    }

    inner class RepositoryEntityDiffCallback(
        private var oldData: List<RepositoryEntity>,
        private var newData: List<RepositoryEntity>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return list[oldItemPosition].repositoryName == newData[oldItemPosition].repositoryName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldData[oldItemPosition].repositoryName == newData[newItemPosition].repositoryName
        }

        override fun getOldListSize() = oldData.size

        override fun getNewListSize() = newData.size

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}