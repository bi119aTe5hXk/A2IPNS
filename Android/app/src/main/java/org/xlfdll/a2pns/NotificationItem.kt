package org.xlfdll.a2pns

import android.os.Parcel
import android.os.Parcelable

class NotificationItem(val title: String, val text: String, val source: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.title)
        dest?.writeString(this.text)
        dest?.writeString(this.source)
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