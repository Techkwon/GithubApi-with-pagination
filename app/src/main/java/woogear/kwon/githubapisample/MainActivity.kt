package woogear.kwon.githubapisample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import woogear.kwon.githubapisample.adapters.PagerAdapter
import woogear.kwon.githubapisample.fragments.FragmentFavorite
import woogear.kwon.githubapisample.fragments.FragmentSearch

class MainActivity : AppCompatActivity() {

    lateinit var adapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(FragmentSearch(), "Search Users")
        adapter.addFragment(FragmentFavorite(), "My Favorite Users")
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)

        view_pager.adapter = adapter

        //from git web
    }
}
