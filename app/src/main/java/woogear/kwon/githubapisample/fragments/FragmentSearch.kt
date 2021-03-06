package woogear.kwon.githubapisample.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search.*
import woogear.kwon.githubapisample.R
import woogear.kwon.githubapisample.adapters.AdapterSearchResult
import woogear.kwon.githubapisample.viewModels.SharedViewModel
import java.lang.Exception

class FragmentSearch : Fragment(){

    private val TAG = "[FragmentSearch]"
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: AdapterSearchResult

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        adapter = AdapterSearchResult(this.context!!, viewModel)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView?.layoutManager = LinearLayoutManager(this.context)
        recyclerView?.adapter = adapter

        setScrollListener()
        observeData()

        searchButton.setOnClickListener { updateList() }
        removeQuery.setOnClickListener { searchText.setText("") }
    }

    private fun updateList(){
        searchText.text.trim(). let {
            if(it.isNotEmpty()) {
                progressbar.visibility = View.VISIBLE
                viewModel.getGithubUsers(it.toString())
            }
        }
    }

    private fun observeData(){
        viewModel.searchLiveData.observe(this, Observer { it ->
            it?.let{
                progressbar.visibility = View.GONE
                adapter.updateList(it.items)
                recyclerView.smoothScrollToPosition(0)
            }
        })

        viewModel.observeMore.observe(this, Observer {
            progressbar.visibility = View.VISIBLE
        })

        viewModel.moreLiveData.observe(this, Observer { it ->
            it?.let{
                progressbar.visibility = View.GONE
                adapter.addList(it.items)
            }
        })
    }

    //listens to scrolling of recycler view
    private fun setScrollListener(){

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(lastVisibleItem, totalItemCount)
            }
        })
    }

}