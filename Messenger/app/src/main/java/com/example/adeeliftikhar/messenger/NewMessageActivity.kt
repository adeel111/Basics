package com.example.adeeliftikhar.messenger

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.example.adeeliftikhar.messenger.Models.RegisterModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.new_msg_rv_design.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        spin_kit_view_new_message.visibility = View.VISIBLE
        fetchUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        val dbRef = FirebaseDatabase.getInstance().getReference("/UsersInfo")
        dbRef.keepSynced(true)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    val user = it.getValue(RegisterModel::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val intent = Intent(view.context, ChatLogActivity::class.java)

                    val userItem = item as UserItem
//                    intent.putExtra(USER_KEY, userItem.user!!.name)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }

                new_message_rv.adapter = adapter
                new_message_rv.addItemDecoration(DividerItemDecoration(this@NewMessageActivity, DividerItemDecoration.VERTICAL))
                spin_kit_view_new_message.visibility = View.GONE
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}

class UserItem(val user: RegisterModel?) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.new_msg_rv_design
    }

    @SuppressLint("RestrictedApi")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text_view_user_name.text = user!!.name

        val comingImage = user.image
        if (comingImage.isEmpty()) {
            return
        } else {
            Picasso.get().load(comingImage).placeholder(R.drawable.common_pic_place_holder)
                .into(viewHolder.itemView.user_circle_image)
        }
    }
}
