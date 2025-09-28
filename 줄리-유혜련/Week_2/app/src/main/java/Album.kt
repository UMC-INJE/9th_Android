package com.umc.myapplication

import android.os.Parcel
import android.os.Parcelable

data class Album(
    val id: Int,
    val title: String,
    val artist: String,
    val coverResId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeInt(coverResId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album = Album(parcel)
        override fun newArray(size: Int): Array<Album?> = arrayOfNulls(size)
    }
}
