package com.h.users.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.h.users.R
import com.h.users.adapters.OnCardListener
import com.h.users.adapters.RecyclerAdapter
import com.h.users.data.User
import com.h.users.viewmodel.ApiStatus
import com.h.users.viewmodel.UsersViewModel
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment : DaggerFragment(), OnCardListener {
    private val TAG = "MainFragment"
    private lateinit var  adapter: RecyclerAdapter
    private lateinit var mUsers: List<User>

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var usersViewModel: UsersViewModel



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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_main, container, false)
        usersViewModel = ViewModelProvider(this, factory)[UsersViewModel::class.java]
        return v
    }



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

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}