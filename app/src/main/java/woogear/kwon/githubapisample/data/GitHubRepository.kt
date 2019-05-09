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

class GitHubRepository(val context: Context) {

    private var requestedPage = 1
    companion object {
        const val itemsPerPage = 20
    }

    private var isRequestInProgress = false
    private var db: DBManager = DBManager(context, "favorite_users.db", null, 1)
    private val TAG: String = "[GitHubRepository]"

    fun searchUser(query: String) : Observable<SearchResponse>{
        val api: APIInterface = APIClient.getClient().create(APIInterface::class.java)
        requestedPage = 1

        return api.getGithubUsers(query, requestedPage, itemsPerPage)
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun requestMore(query: String, totalPage: Int) : Observable<SearchResponse>?{

        if (isRequestInProgress) return null
        isRequestInProgress = true

        val api: APIInterface = APIClient.getClient().create(APIInterface::class.java)

        if(requestedPage < totalPage) requestedPage++ else return null

        Log.d(TAG, "currentPage: $requestedPage,  totalPage: $totalPage")
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

    fun contains(user: String) : Boolean {
        return db.contains(user)
    }

    fun delete(user: String){
        db.deleteUser(user)
    }

}