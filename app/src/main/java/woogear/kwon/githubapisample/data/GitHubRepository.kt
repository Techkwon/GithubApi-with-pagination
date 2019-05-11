package woogear.kwon.githubapisample.data

import android.content.Context
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import woogear.kwon.githubapisample.model.GithubUser
import woogear.kwon.githubapisample.model.SearchResponse
import woogear.kwon.githubapisample.utils.APIClient
import woogear.kwon.githubapisample.utils.APIInterface
import java.util.concurrent.TimeUnit

/**
 * GitHubRepository class handles data from extra web API or from inner SQLite db
 * */

class GitHubRepository(private val context: Context) {

    private val TAG: String = "[GitHubRepository]"
    private var requestedPage = 1
    private var isRequestInProgress = false

    companion object {
        const val itemsPerPage = 20
    }

    private var db: DBManager = DBManager(context, "favorite_users.db", null, 1)

    fun searchUser(query: String) : Observable<SearchResponse>{
        val api: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        requestedPage = 1 //initial page number for every searching results, it increases whenever the scroll arrives at the last position.

        return api.getGithubUsers(query, requestedPage, itemsPerPage)
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun requestMore(query: String, totalPage: Int) : Observable<SearchResponse>?{

        if (isRequestInProgress) return null
        isRequestInProgress = true

        if(requestedPage < totalPage) requestedPage ++ else return null

        Log.d(TAG, "currentPage: $requestedPage,  totalPage: $totalPage")

        val api: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        return api.getGithubUsers(query, requestedPage, itemsPerPage)
            .delay(1, TimeUnit.SECONDS)
            .doOnError {isRequestInProgress = false}
            .doOnComplete {isRequestInProgress = false}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addFavorite(user: GithubUser){
        db.insertUser(user)
    }

    fun getFavoriteUsers() : List<GithubUser>{
        return db.selectUsers()
    }

    fun isSaved(user: String) : Boolean {
        return db.isSaved(user)
    }

    fun deleteUser(user: String){
        db.deleteUser(user)
    }

}