package pt.isel.pdm.nasaimageoftheday.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize // -> add id("kotlin-parcelize") to the project gradle plugins
data class NasaImage(
    val title: String,
    val author: String?,
    val description: String,
    val date: String,
    val url: String,
) : Parcelable {
}

