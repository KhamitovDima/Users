package com.h.users.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.h.users.R
import com.h.users.adapters.OnCardListener
import com.h.users.adapters.RecyclerAdapter
import com.h.users.data.User
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(R.layout.fragment_main), OnCardListener {
    private val TAG = "MainFragment"
    private lateinit var  adapter: RecyclerAdapter
    private lateinit var mUsers: List<User>



    override fun onCardClick(position: Int) {
        val userFragment = UserFragment.newInstance()
        val bundle = Bundle()
        bundle.putParcelable("CurrentUser", mUsers[position])
        userFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, userFragment)
            .addToBackStack(null)
            .commit()

    }

    private val usersViewModel by viewModels<UsersViewModel>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.layoutManager = LinearLayoutManager(context)
        adapter = RecyclerAdapter(this)
        recycler_view.adapter = adapter
        observeData()
        usersViewModel.loadIfDbIsEmpty()

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recycler_view.canScrollVertically(1)) {
                    Log.d(TAG, "onScrollStateChanged")
                    usersViewModel.searchNextPage()
                }
            }
        })

        fab.setOnClickListener {
            usersViewModel.updateUsers()
        }
        
    }

    fun observeData() {
        usersViewModel.getUsers().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mUsers = it
                adapter.setUsersInAdapter(mUsers)
            }

        })

        usersViewModel.status.observe(viewLifecycleOwner, Observer {
            if (usersViewModel.status.value == ApiStatus.ERROR) {
                Toast.makeText(context, "Проверьте ваше подключение...", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}