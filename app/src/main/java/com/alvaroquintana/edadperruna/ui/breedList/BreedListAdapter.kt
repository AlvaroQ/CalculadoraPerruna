package com.alvaroquintana.edadperruna.ui.breedList

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.alvaroquintana.domain.Dog
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.common.basicDiffUtil
import com.alvaroquintana.edadperruna.common.inflate
import com.alvaroquintana.edadperruna.utils.glideLoadURL

class BreedListAdapter(private var context: Context,
                       private val clickListener: (Int, Dog) -> Unit,
                       private val longClickListener: (ImageView, String) -> Unit,
) : RecyclerView.Adapter<BreedListAdapter.BreedListViewHolder>() {
    var data: List<Dog> by basicDiffUtil(areItemsTheSame = { old, new -> old.breedId == new.breedId })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedListViewHolder {
        val view = parent.inflate(R.layout.item_breed, false)
        return BreedListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedListViewHolder, position: Int) {
        val breed = data[position]

        holder.dogName.text = breed.name
        glideLoadURL(context,  breed.image, holder.dogImage)
        holder.itemContainer.setOnClickListener { clickListener(position, breed) }
        holder.itemContainer.setOnLongClickListener {
            longClickListener(holder.dogImage, breed.image!!)
            true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int): Dog {
        return data[position]
    }

    class BreedListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dogName: TextView = view.findViewById(R.id.dogName)
        var dogImage: ImageView = view.findViewById(R.id.dogImage)
        var itemContainer: ConstraintLayout = view.findViewById(R.id.itemContainer)
    }
}