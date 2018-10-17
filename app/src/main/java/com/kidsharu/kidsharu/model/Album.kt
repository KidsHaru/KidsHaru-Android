package com.kidsharu.kidsharu.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

open class Album(
        val albumId: Int,
        val title: String,
        val content: String,
        val date: String,
        val status: AlbumStatus
) : Parcelable {
    override fun toString(): String {
        return "Album(albumId=$albumId, title='$title', content='$content', date='$date', status='$status')"
    }

    constructor(json: JSONObject) : this(
            json.getInt("album_id"),
            json.getString("title"),
            json.getString("content"),
            json.getString("date"),
            AlbumStatus.valueOf(json.getString("status"))
    )

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            AlbumStatus.valueOf(source.readString())
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(albumId)
        writeString(title)
        writeString(content)
        writeString(date)
        writeString(status.toString())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Album> = object : Parcelable.Creator<Album> {
            override fun createFromParcel(source: Parcel): Album = Album(source)
            override fun newArray(size: Int): Array<Album?> = arrayOfNulls(size)
        }
    }
}

@Suppress("EnumEntryName")
enum class AlbumStatus {
    uploading,
    processing,
    checking,
    done;
}