package com.h.users.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.h.users.R
import com.h.users.data.User
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : Fragment(R.layout.fragment_user) {

    private var currentUser: User? = null

    private val usersViewModel by viewModels<UsersViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = arguments?.getParcelable("CurrentUser")
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

    companion object {
        fun newInstance() = UserFragment()
    }
}