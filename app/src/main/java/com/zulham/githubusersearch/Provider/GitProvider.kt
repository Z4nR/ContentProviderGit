package com.zulham.githubusersearch.Provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.zulham.githubusersearch.Database.db.DatabaseContract.AUTHOR
import com.zulham.githubusersearch.Database.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.zulham.githubusersearch.Database.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.zulham.githubusersearch.Database.db.FavHelper
import kotlinx.coroutines.InternalCoroutinesApi

class GitProvider : ContentProvider() {

    companion object {
        private const val Git = 1
        private const val Git_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favHelper: FavHelper
        init {
            sUriMatcher.addURI(AUTHOR, TABLE_NAME, Git)
            sUriMatcher.addURI(AUTHOR,
                    "$TABLE_NAME/#",
                    Git_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (Git) {
            sUriMatcher.match(uri) -> favHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    @InternalCoroutinesApi
    override fun onCreate(): Boolean {
        favHelper = FavHelper.getInstance(context as Context)
        favHelper.open()
        return true
    }

    override fun query(
            uri: Uri, projection: Array<String>?, selection: String?,
            selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            Git -> favHelper.queryAll()
            Git_ID -> favHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun update(
            uri: Uri, values: ContentValues?, selection: String?,
            selectionArgs: Array<String>?,
    ): Int {
        return 0
    }
}