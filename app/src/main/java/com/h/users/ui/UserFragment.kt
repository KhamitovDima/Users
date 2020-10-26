package com.h.users.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.h.users.R
import com.h.users.data.User
import com.h.users.viewmodel.UsersViewModel
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_user.*
import javax.inject.Inject


class UserFragment : DaggerFragment() {

    private var currentUser: User? = null

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var usersViewModel: UsersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getParcelable("CurrentUser")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_user, container, false)
        usersViewModel = ViewModelProvider(this, factory)[UsersViewModel::class.java]
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        first_name.setText(currentUser?.first_name)
        last_name.setText(currentUser?.last_name)
        email.setText(currentUser?.email)
        val requestOptions = RequestOptions()
            .placeholder(R.color.white)
        Glide.with(requireContext())
            .setDefaultRequestOptions(requestOptions)
            .load(currentUser?.avatar)
            .into(imageView)

        save.setOnClickListener {
            usersViewModel.saveUser(currentUser!!)
            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()
        }

        delete.setOnClickListener {
            usersViewModel.deleteUser(currentUser?.id.toString())
            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()
        }

        save.setOnClickListener {
            val user = User(
                currentUser?.id!!,
                email.text.toString(),
                first_name.text.toString(),
                last_name.text.toString(),
                currentUser?.avatar!!
            )
            usersViewModel.saveUser(user)
            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()
        }


    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    companion object {
        fun newInstance() = UserFragment()
    }
}