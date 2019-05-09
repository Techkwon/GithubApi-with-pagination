package woogear.kwon.githubapisample.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import woogear.kwon.githubapisample.R
import woogear.kwon.githubapisample.data.DBManager
import woogear.kwon.githubapisample.model.GithubUser
import woogear.kwon.githubapisample.viewModels.MainViewModel

class AdapterSearchResult(
    private val context: Context,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<AdapterSearchResult.ViewHolder>() {

    private val list = ArrayList<GithubUser>()
    private var db: DBManager = DBManager(context, "favorite_users.db", null, 1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageUrl: String = list[position].avatar_url
        val userLogin: String = list[position].login
        val score: Float = list[position].score

        Glide.with(context)
            .load(imageUrl)
            .into(holder.avatar)

        holder.login.text = "id: $userLogin"
        holder.score.text = "score: $score"

        holder.checkbox.isChecked = viewModel.contains(userLogin)

        holder.checkbox.setOnClickListener {
            if(!holder.checkbox.isChecked) viewModel.delete(userLogin) else viewModel.addFavoriteUsers(list[position])
        }
    }

    fun updateList(data: List<GithubUser>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun addList(data: List<GithubUser>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){

        val checkbox: CheckBox = v.checkbox
        val avatar: ImageView = v.userPhoto
        val login: TextView = v.userName
        val score: TextView = v.score
    }

}