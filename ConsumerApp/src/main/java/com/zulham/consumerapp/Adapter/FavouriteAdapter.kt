package com.zulham.consumerapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zulham.consumerapp.R
import com.zulham.githubusersearch.Database.entity.FavUser
import kotlinx.android.synthetic.main.user.view.*

class FavouriteAdapter(private val favs: ArrayList<FavUser>) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    var listFavUser = ArrayList<FavUser>()
        set(listFavUser) {
            if (listFavUser.size > 0) {
                this.listFavUser.clear()
            }
            this.listFavUser.addAll(listFavUser)
            notifyDataSetChanged()
        }

    fun addItem(fav: FavUser) {
        this.listFavUser.add(fav)
        notifyItemInserted(this.listFavUser.size - 1)
    }

    fun updateItem(position: Int, fav: FavUser) {
        this.listFavUser[position] = fav
        notifyItemChanged(position, fav)
    }

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favUser: FavUser) {
            with(itemView){
                Glide.with(itemView.context)
                        .load(favUser.imgUser)
                        .apply(RequestOptions().override(55,55))
                        .into(userImg)

                userId.text = favUser.UserName
                IDuser.text = favUser.IDUser.toString()
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