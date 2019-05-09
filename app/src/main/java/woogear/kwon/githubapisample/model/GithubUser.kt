package woogear.kwon.githubapisample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class GithubUser (

    val avatar_url: String,
    val login: String,
    val score: Float

) : Parcelable