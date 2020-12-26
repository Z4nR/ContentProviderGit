package com.zulham.githubusersearch.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zulham.githubusersearch.Database.entity.FavUser
import com.zulham.githubusersearch.R
import kotlinx.android.synthetic.main.user.view.*

class FavouriteAdapter(private val favs: ArrayList<FavUser>) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavUser)
    }

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favUser: FavUser) {
            with(itemView){
                Glide.with(itemView.context)
                        .load(favUser.imgUser)
                        .apply(RequestOptions().override(55,55))
                        .into(userImg)

                userId.text = favUser.login
                IDuser.text = favUser.IDUser.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(favUser) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favs[position])
    }

    override fun getItemCount(): Int = favs.size


}