package com.example.adeeliftikhar.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adeeliftikhar.messenger.Models.ChatMessageModel
import com.example.adeeliftikhar.messenger.Models.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.latest_message_row.view.*
import kotlinx.android.synthetic.main.new_msg_rv_design.view.*

class ChatLogActivity : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    var toUser: RegisterModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recycler_view_chat_log.adapter = adapter

//        val userName = intent.getStringExtra(NewMessageActivity.USER_KEY)
        toUser = intent.getParcelableExtra<RegisterModel>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.name

        getMessagesFromDB()
        button_send_message.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage() {
        val text = edit_text_enter_message.text.toString().trim()
        val fromID = FirebaseAuth.getInstance().uid
        val toUser = intent.getParcelableExtra<RegisterModel>(NewMessageActivity.USER_KEY)
        val toID = toUser.id

        val dbRef = FirebaseDatabase.getInstance().getReference("/UserMessages/$fromID/$toID").push()

        val toRef = FirebaseDatabase.getInstance().getReference("/UserMessages/$toID/$fromID").push()

        val chatMessage = ChatMessageModel(dbRef.key!!, text, fromID!!, toID, System.currentTimeMillis() / 1000)
        dbRef.setValue(chatMessage).addOnSuccessListener {
            edit_text_enter_message.text.clear()
            recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
        }
        toRef.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/LatestMessage/$fromID/$toID")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/LatestMessage/$toID/$fromID")
        latestMessageToRef.setValue(chatMessage)
    }

    private fun getMessagesFromDB() {
        val fromID = FirebaseAuth.getInstance().uid
        val toID = toUser?.id
        val dbRef = FirebaseDatabase.getInstance().getReference("/UserMessages/$fromID/$toID")

        dbRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessageModel::class.java)
                if (chatMessage != null) {
                    if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
                        val currentUser = MainActivity.currentUser
                        adapter.add(ChatFromItem(chatMessage.text, currentUser!!))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }

                recycler_view_chat_log.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}

class ChatFromItem(val text: String, val user: RegisterModel) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text_view_from_row.text = text
        val imageUri = user.image
        if (imageUri.isEmpty()) {
            return
        } else {
            Picasso.get().load(imageUri).placeholder(R.drawable.common_pic_place_holder)
                .into(viewHolder.itemView.image_view_from_row)
        }
    }
}

class ChatToItem(val text: String, val user: RegisterModel) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text_view_to_row.text = text
        val imageUri = user.image
        if (imageUri.isEmpty()) {
            return
        } else {
            Picasso.get().load(imageUri).placeholder(R.drawable.common_pic_place_holder)
                .into(viewHolder.itemView.image_view_to_row)
        }
    }
}
