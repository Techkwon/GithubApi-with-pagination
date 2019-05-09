package woogear.kwon.githubapisample.utils

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import woogear.kwon.githubapisample.model.SearchResponse

interface APIInterface {

    @GET("/search/users")
    fun getGithubUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Observable<SearchResponse>


}