package com.application.igate.visitorDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.igate.R
import com.application.igate.gone
import com.application.igate.model.visitor.Visitor
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_visitor_details.view.*

class VisitorDetailsAdapter(
    private val context: Context,
    private val visitors: Array<Visitor>
) : RecyclerView.Adapter<VisitorDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitorDetailsViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_visitor_details, parent, false)
        return VisitorDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return visitors.size
    }

    override fun onBindViewHolder(holder: VisitorDetailsViewHolder, position: Int) {
        val visitor = visitors[position]
        holder.bind(visitor, context)
    }
}

class VisitorDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(visitor: Visitor, context: Context) {
        itemView.visitorName.text = visitor.visitorName
        if (visitor.visitorEmailId.isEmpty()) itemView.visitorEmail.gone()
        else itemView.visitorEmail.text = visitor.visitorEmailId
        if (visitor.visitorAddress.isEmpty()) itemView.visitorAddress.gone()
        else itemView.visitorAddress.text = visitor.visitorAddress
        itemView.contactNumber.text = visitor.phoneNumber
        itemView.visitorPurpose.text = visitor.visitorPurpose
        itemView.visitingTime.text = visitor.visitingTime
        itemView.visitorMeeting.text = visitor.meetingFlatNo
        loadImage(visitor.visitorPhoto, context)
    }

    private fun loadImage(visitorPhoto: String, context: Context) {
        Glide.with(context)
            .load(visitorPhoto.toByteArray())
            .placeholder(R.drawable.profile_pic)
            .into(itemView.visitor_photo)
    }
}