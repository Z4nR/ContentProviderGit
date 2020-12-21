package com.zulham.githubusersearch.View

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zulham.githubusersearch.Adapter.FavouriteAdapter
import com.zulham.githubusersearch.Database.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.zulham.githubusersearch.Database.db.FavHelper
import com.zulham.githubusersearch.Database.entity.FavUser
import com.zulham.githubusersearch.Database.helper.MappingHelper
import com.zulham.githubusersearch.R
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity() {

    private val listFav = ArrayList<FavUser>()
    private lateinit var progressBar: ProgressBar
    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        favHelper = FavHelper(applicationContext)

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
        contentResolver.registerContentObserver( CONTENT_URI,true, myObserver)

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

        favouriteAdapter.setOnItemClickCallback(object : FavouriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavUser) {
                val intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                val user = data
                intent.putExtra("user", user)
                startActivity(intent)
            }

        })

    }

    private fun setUpToolbar() {
        supportActionBar?.setTitle("Favourite User")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
}