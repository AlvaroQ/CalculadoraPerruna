package com.alvaroquintana.edadperruna.ui.moreApps

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alvaroquintana.domain.App
import com.alvaroquintana.edadperruna.R
import com.alvaroquintana.edadperruna.common.inflate
import com.alvaroquintana.edadperruna.utils.glideLoadBase64
import com.alvaroquintana.edadperruna.utils.openAppOnPlayStore

class MoreAppsListAdapter(
    val context: Context,
    var rankingList: MutableList<App>) : RecyclerView.Adapter<MoreAppsListAdapter.RankingListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingListViewHolder {
        val view = parent.inflate(R.layout.item_ranking_user, false)
        return RankingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingListViewHolder, position: Int) {
        val app = rankingList[position]
        holder.appName.text = app.name
        holder.appDecription.text = app.description
        glideLoadBase64(context,  app.image, holder.appImage)
        holder.itemContainer.setOnClickListener {
            openAppOnPlayStore(context, app.url!!)
        }
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }

    fun getItem(position: Int): App {
        return rankingList[position]
    }

    class RankingListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appName: TextView = view.findViewById(R.id.nameText)
        var appDecription: TextView = view.findViewById(R.id.descriptionText)
        var appImage: ImageView = view.findViewById(R.id.appImage)
        var itemContainer: CardView = view.findViewById(R.id.itemContainer)
    }
}