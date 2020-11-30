package com.alvaroquintana.edadperruna.ui.result

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

class AppListAdapter(private var context: Context,
                     var appList: MutableList<App>,
                     private val clickListener: (String) -> Unit,
) : RecyclerView.Adapter<AppListAdapter.AppListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        val view = parent.inflate(R.layout.item_app, false)
        return AppListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        val app = appList[position]
        holder.appName.text = app.name
        glideLoadBase64(context,  app.image, holder.appImage)
        holder.itemContainer.setOnClickListener { clickListener(app.url!!) }
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    fun getItem(position: Int): App {
        return appList[position]
    }

    class AppListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var appName: TextView = view.findViewById(R.id.appName)
        var appImage: ImageView = view.findViewById(R.id.appImage)
        var itemContainer: CardView = view.findViewById(R.id.itemContainer)
    }
}