package com.wms.app.universal.user.ui.activity

import android.app.Activity
import android.content.CursorLoader
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import com.wms.app.universal.R
import com.wms.app.universal.user.model.LoginViewHolder
import com.wms.base.common.BaseActivity
import com.wms.base.sdk.createView
import java.util.ArrayList

class LoginActivity: BaseActivity(), LoaderManager.LoaderCallbacks<Cursor>, TextView.OnEditorActionListener {
    private var holder: LoginViewHolder? = null

    companion object {
        fun start(ac: Activity) {
            val intent = Intent(ac, LoginActivity::class.java)
            ac.startActivity(intent)
        }
    }

    override fun slidEnable(): Boolean {
        return true
    }


    override fun getLayout(): View {
        return createView(R.layout.activity_login)
    }

    override fun initView() {
        holder = LoginViewHolder(getContentView())
        holder!!.init("登录")
        holder!!.login.setOnClickListener { attemptLogin() }
        holder!!.password.setOnEditorActionListener(this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
            attemptLogin()
            return true
        }
        return false
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        val uri = Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                ContactsContract.Contacts.Data.CONTENT_DIRECTORY)

        val projection = ProfileQuery.PROJECTION
        val selection = ContactsContract.Contacts.Data.MIMETYPE + " = ?"
        val selectionArgs = arrayOf(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
        val sortOrder = ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"

        val cursorLoader = CursorLoader(this, uri, projection, selection, selectionArgs, sortOrder)

        return cursorLoader as Loader<Cursor>
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(p0: Loader<Cursor>) {

    }

    private interface ProfileQuery {
        companion object {
            val PROJECTION = arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY)

            val ADDRESS = 0
            val IS_PRIMARY = 1
        }
    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        val adapter = ArrayAdapter(this@LoginActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)
        holder!!.email.setAdapter(adapter)
    }

    private fun attemptLogin() {
        holder!!.email.error = null
        holder!!.password.error = null

        // Store values at the time of the login attempt.
        val email = holder!!.email.text.toString()
        val password = holder!!.password.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            holder!!.password.error = (getString(R.string.error_invalid_password))
            focusView = holder!!.password
            cancel = true
        }

        if (TextUtils.isEmpty(email)) {
            holder!!.email.error = (getString(R.string.error_field_required))
            focusView = holder!!.email
            cancel = true
        } else if (!isEmailValid(email)) {
            holder!!.email.error = (getString(R.string.error_invalid_email))
            focusView = holder!!.email
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}