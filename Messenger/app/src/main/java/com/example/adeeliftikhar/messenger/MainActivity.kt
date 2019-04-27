package com.example.adeeliftikhar.messenger

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.adeeliftikhar.messenger.Internet.CheckInternetConnectivity
import com.example.adeeliftikhar.messenger.Models.ChatMessageModel
import com.example.adeeliftikhar.messenger.Models.RegisterModel
import com.example.adeeliftikhar.messenger.SessionsManager.LoginSessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class MainActivity : AppCompatActivity() {
    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessageMap = HashMap<String, ChatMessageModel>()

    companion object {
        var currentUser: RegisterModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spin_kit_view_latest_message.visibility = View.VISIBLE
        recycler_view_latest_messages.adapter = adapter
        recycler_view_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        getLatestMessage()
        fetchCurrentUser()
    }

    private fun refreshRecyclerViewMessage() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageRow(chatMessage = it))
        }
        if (adapter.itemCount != 0) {
            spin_kit_view_latest_message.visibility = View.GONE
        }
    }

    private fun getLatestMessage() {
        val fromID = FirebaseAuth.getInstance().uid
        val dbRef = FirebaseDatabase.getInstance().getReference("/LatestMessage/$fromID")
        dbRef.keepSynced(true)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    text_view_no_chat.visibility = View.VISIBLE
                    spin_kit_view_latest_message.visibility = View.GONE
                } else {
                    dbRef.addChildEventListener(object : ChildEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                        }

                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                            val chatMessage = p0.getValue(ChatMessageModel::class.java) ?: return
                            latestMessageMap[p0.key!!] = chatMessage
                            refreshRecyclerViewMessage()
                        }

                        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                            val chatMessage = p0.getValue(ChatMessageModel::class.java) ?: return
                            latestMessageMap[p0.key!!] = chatMessage

                            refreshRecyclerViewMessage()
                        }

                        override fun onChildRemoved(p0: DataSnapshot) {
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val dbRef = FirebaseDatabase.getInstance().getReference("/UsersInfo/$uid")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(RegisterModel::class.java)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_message -> {
                if (!CheckInternetConnectivity.isConnected(this)) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, NewMessageActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.logout -> {
                logoutAlertBox()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutAlertBox() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Logout")
        builder.setMessage("Do you want to Logout?")
        builder.setPositiveButton("Logout") { dialog, which ->
            if (!CheckInternetConnectivity.isConnected(this@MainActivity)) {
                Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                dialogBoxLogout()
                logout()
            }
        }.setNegativeButton("Cancel") { dialog, which -> }
        builder.show()
    }

    private fun dialogBoxLogout() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logout in Progress.")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun logout() {
        val loginSessionManager = LoginSessionManager(this@MainActivity)
        loginSessionManager.loginTheUser(false, "", "")
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
