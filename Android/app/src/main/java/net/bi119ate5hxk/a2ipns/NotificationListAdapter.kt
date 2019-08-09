package net.bi119ate5hxk.a2ipns

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationListAdapter(private val notificationItemList: ArrayList<NotificationItem>) :
    RecyclerView.Adapter<NotificationListAdapter.NotificationListViewHolder>() {
    class NotificationListViewHolder(private val itemView: LinearLayout) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = notificationItemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_notification_item, parent, false) as LinearLayout

        return NotificationListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificationListViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.notificationTitleTextView).text =
            notificationItemList[position].title
        holder.itemView.findViewById<TextView>(R.id.notificationTextTextView).text = notificationItemList[position].text
        holder.itemView.findViewById<TextView>(R.id.notificationSourceTextView).text =
            notificationItemList[position].source
    }
}