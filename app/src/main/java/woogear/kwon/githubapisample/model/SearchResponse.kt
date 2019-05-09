package woogear.kwon.githubapisample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SearchResponse (

    val total_count: Int,
    val incomplete_result: Boolean,
    val items: List<GithubUser>


) : Parcelable