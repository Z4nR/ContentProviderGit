package com.zulham.consumerapp.View

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zulham.consumerapp.Adapter.FavouriteAdapter
import com.zulham.consumerapp.R
import com.zulham.githubusersearch.Database.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.zulham.githubusersearch.Database.entity.FavUser
import com.zulham.githubusersearch.Database.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main_favourite.*

class MainFavourite : AppCompatActivity() {

    private val listFav = ArrayList<FavUser>()
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_favourite)

        progressBar = findViewById(R.id.progressBar)

        rv_FavUser.setHasFixedSize(true)

        setUpToolbar()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                recycleFavUser()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI,true, myObserver)
        
        if (savedInstanceState == null) {
            recycleFavUser()
        }

    }

    private fun getData(){
        val query = contentResolver.query(CONTENT_URI, null, null, null, null)
        val mapping = MappingHelper.mapCursorToArrayList(query)

        if (query != null) {
            if (query.count > 0){

                progressBar.visibility = View.GONE

                listFav.addAll(mapping)

            }
        }
    }

    private fun recycleFavUser() {

        getData()

        rv_FavUser.layoutManager = LinearLayoutManager(this)

        val favouriteAdapter = FavouriteAdapter(listFav)

        rv_FavUser.adapter = favouriteAdapter

    }

    private fun setUpToolbar() {
        supportActionBar?.setTitle("Favourite User")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}