package woogear.kwon.githubapisample.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_search.*
import woogear.kwon.githubapisample.R
import woogear.kwon.githubapisample.adapters.AdapterFavorite
import woogear.kwon.githubapisample.data.DBManager
import woogear.kwon.githubapisample.model.GithubUser
import woogear.kwon.githubapisample.viewModels.MainViewModel
import java.lang.Exception

class FragmentFavorite : Fragment(){

    private val TAG = "[FragmentFavorite]"
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: AdapterFavorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        viewModel.getFavoriteUsers()
        adapter = AdapterFavorite(this.context!!, viewModel)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavorite?.layoutManager = LinearLayoutManager(this.context)
        rvFavorite?.adapter = adapter
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.favoriteLiveData.observe(this, Observer { it ->
            it?.let{
                adapter.updateList(it)
            }
        })
    }

}