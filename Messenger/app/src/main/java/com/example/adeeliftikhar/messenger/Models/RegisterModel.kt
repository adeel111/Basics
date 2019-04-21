package com.example.adeeliftikhar.messenger.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class RegisterModel(var name: String = "", var email: String = "", var password: String = "", var image: String = "") :
    Parcelable {
    constructor() : this("", "", "", "")
}