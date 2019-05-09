package woogear.kwon.githubapisample.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import woogear.kwon.githubapisample.model.SearchResponse
import woogear.kwon.githubapisample.data.GitHubRepository
import woogear.kwon.githubapisample.model.GithubUser


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val TAG: String = "[MainViewModel]"

    private val repository = GitHubRepository(application.applicationContext)

    val searchLiveData = MutableLiveData<SearchResponse>()
    val moreLiveData = MutableLiveData<SearchResponse>()
    val observeMore = MutableLiveData<Boolean>()
    val users = ArrayList<GithubUser>()
    val favoriteLiveData = MutableLiveData<ArrayList<GithubUser>>()

    private val disposable = CompositeDisposable()
    private val queryLiveData = MutableLiveData<String>()
    private val totalPageLiveData = MutableLiveData<Int>()

    fun getGithubUsers(query: String){

        queryLiveData.postValue(query)
        disposable.add(repository.searchUser(query)
            .subscribe {
                searchLiveData.postValue(it)

                if (it.total_count % GitHubRepository.itemsPerPage == 0) {
                    totalPageLiveData.postValue(it.total_count / GitHubRepository.itemsPerPage)
                } else {
                    totalPageLiveData.postValue((it.total_count / GitHubRepository.itemsPerPage) + 1)
                }
            })
    }

    fun listScrolled(lastVisibleItemPosition: Int, totalItemCount: Int){

        if(lastVisibleItemPosition + 1 == totalItemCount) {
            val immutableQuery = getLastQuery()

            if(immutableQuery != null){
                Log.d(TAG, "Last Query : $immutableQuery")
                getMoreUsers(immutableQuery)
            }
        }
    }

    fun addFavoriteUsers(user: GithubUser){
        repository.addFavorite(user)
        users.clear()
        users.addAll(repository.getFavoriteUsers())
        favoriteLiveData.postValue(users)
    }

    fun getFavoriteUsers(){
        users.clear()
        users.addAll(repository.getFavoriteUsers())
        favoriteLiveData.postValue(users)

    }

    private fun getMoreUsers(immutableQuery: String){

        val observable = repository.requestMore(immutableQuery, totalPageLiveData.value!!)
        if(observable != null){
            disposable.add(observable
                .doOnSubscribe {observeMore.postValue(true)}
                .subscribe{
                    moreLiveData.postValue(it)
                })
        } else {
            //no more results..
        }
    }

    fun contains(user: String) : Boolean {
        return repository.contains(user)
    }

    fun delete(user: String) {
        repository.delete(user)
        users.clear()
        users.addAll(repository.getFavoriteUsers())
        favoriteLiveData.postValue(users)
    }

    private fun getLastQuery(): String? = queryLiveData.value

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}