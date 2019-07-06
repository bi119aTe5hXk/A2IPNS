package net.bi119ate5hxk.a2ipns

import android.os.Parcel
import android.os.Parcelable

class NotificationItem(title: String, text: String, source: String) : Parcelable {
    val Title: String = title
    val Text: String = text
    val Source: String = source

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.Title)
        dest?.writeString(this.Text)
        dest?.writeString(this.Source)
    }

    companion object CREATOR : Parcelable.Creator<NotificationItem> {
        override fun createFromParcel(parcel: Parcel): NotificationItem {
            return NotificationItem(parcel)
        }

        override fun newArray(size: Int): Array<NotificationItem?> {
            return arrayOfNulls(size)
        }
    }


}