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
import com.alvaroquintana.edadperruna.common.inflate
import com.alvaroquintana.edadperruna.ui.components.AspectRatioImageView
import com.alvaroquintana.edadperruna.utils.glideLoadBase64

class BreedListAdapter(private var context: Context,
                       var breedList: MutableList<Dog>,
                       private val clickListener: (Dog) -> Unit,
                       private val longClickListener: (ImageView, String) -> Unit,
) : RecyclerView.Adapter<BreedListAdapter.BreedListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedListViewHolder {
        val view = parent.inflate(R.layout.item_breed, false)
        return BreedListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BreedListViewHolder, position: Int) {
        val breed = breedList[position]
        holder.dogName.text = breed.name
        glideLoadBase64(context,  breed.icon, holder.dogImage)
        holder.itemContainer.setOnClickListener { clickListener(breed) }
        holder.itemContainer.setOnLongClickListener {
            longClickListener(holder.dogImage, breed.icon!!)
            true
        }
    }

    override fun getItemCount(): Int {
        return breedList.size
    }

    fun getItem(position: Int): Dog {
        return breedList[position]
    }

    class BreedListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dogName: TextView = view.findViewById(R.id.dogName)
        var dogImage: AspectRatioImageView = view.findViewById(R.id.dogImage)
        var itemContainer: ConstraintLayout = view.findViewById(R.id.itemContainer)
    }
}