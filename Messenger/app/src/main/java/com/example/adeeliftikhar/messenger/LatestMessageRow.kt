package com.example.adeeliftikhar.messenger

import com.example.adeeliftikhar.messenger.Models.ChatMessageModel
import com.example.adeeliftikhar.messenger.Models.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessageModel) : Item<ViewHolder>() {
    var chatPartnerUser: RegisterModel? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.text_view_latest_message.text = chatMessage.text
        val chatPartnerID: String?
        if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
            chatPartnerID = chatMessage.toID
        } else {
            chatPartnerID = chatMessage.fromID
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("/UsersInfo/$chatPartnerID")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(RegisterModel::class.java)
                viewHolder.itemView.text_view_latest_msg_user.text = chatPartnerUser?.name

                val imageUri = chatPartnerUser?.image
                Picasso.get().load(imageUri).placeholder(R.drawable.common_pic_place_holder)
                    .into(viewHolder.itemView.image_view_user_latest)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}