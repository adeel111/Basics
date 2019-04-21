package com.example.adeeliftikhar.messenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.adeeliftikhar.messenger.Models.RegisterModel

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

//        val userName = intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user = intent.getParcelableExtra<RegisterModel>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.name

    }
}
