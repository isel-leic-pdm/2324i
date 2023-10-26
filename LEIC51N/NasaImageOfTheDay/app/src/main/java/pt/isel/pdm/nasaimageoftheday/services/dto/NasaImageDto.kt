package pt.isel.pdm.nasaimageoftheday.services.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import pt.isel.pdm.nasaimageoftheday.model.NasaImage

data class NasaImageDto(
    val copyright: String,
    @SerializedName("explanation")
    val description: String,
    val date: String,
    val url: String,
    val title: String
)  {


    companion object {
        fun toNasaImage(remoteNasa: NasaImageDto): NasaImage {
            return NasaImage(
                title = remoteNasa.title,
                description = remoteNasa.description,
                date = remoteNasa.date,
                url = remoteNasa.url,
                author = remoteNasa.copyright
            );
        }
    }

}
