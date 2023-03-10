package com.app.func.login_demo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.ProfileFragmentBinding
import com.app.func.features.room_database.ListClickListener
import com.app.func.features.room_database.User
import com.app.func.features.room_database.UserListAdapter
import com.app.func.features.room_database.UserRepository

class ProfileFragment : BaseFragment() {

    private var binding: ProfileFragmentBinding? = null
    var user: User? = null

    private var userAdapter = UserListAdapter()
    private val repo: UserRepository by lazy {
        UserRepository(requireContext())
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private fun fetchUsers() {
        val allUsers = repo.getAllUsers()
        allUsers?.let { userAdapter.setUsers(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        binding?.btnAdd?.setOnClickListener {
            addNewUser()
        }

        binding?.btnUpdate?.setOnClickListener {
            updateUser()
        }

        userAdapter.setOnItemClick(object : ListClickListener<User> {
            override fun onClick(data: User, position: Int) {
                //binding?.edUsername?.text = data.userName
                binding?.edUsername?.setText(data.userName)
                //binding?.edEmail?.text = data.email
                binding?.edEmail?.setText(data.email)
                //binding?.edLocation?.text = data.location
                binding?.edLocation?.setText(data.location)
                user = data
                binding?.btnUpdate?.isEnabled = true
            }

            override fun onDelete(user: User) {
                AlertDialog.Builder(context)
                    .setTitle("Delete user")
                    .setMessage("Are you sure you want to delete this user?")
                    .setPositiveButton(
                        "Yes"
                    ) { dialog, which ->
                        repo.deleteUser(user)
                        fetchUsers()
                    } // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("No", null)
                    .setIcon(R.drawable.ic_white_delete)
                    .show()
            }
        })

        binding?.recyclerviewUsers?.layoutManager = LinearLayoutManager(requireContext())
        binding?.recyclerviewUsers?.adapter = userAdapter
        fetchUsers()
        return binding?.root
    }

    private fun updateUser() {
        if (binding?.edUsername?.text?.isNotEmpty() == true
            && binding?.edEmail?.text?.isNotEmpty() == true
            && binding?.edLocation?.text?.isNotEmpty() == true
        ) {
            val user = User(
                userId = user?.userId,
                userName = binding?.edUsername?.text.toString(),
                location = binding?.edLocation?.text.toString(),
                email = binding?.edEmail?.text.toString()
            )
            repo.updateUser(user)
            fetchUsers()
        } else {
            Toast.makeText(requireContext(), "Invalid Input", Toast.LENGTH_SHORT).show()
        }
        binding?.btnUpdate?.isEnabled = false
    }

    private fun addNewUser() {
        if (binding?.edUsername?.text?.isNotEmpty() == true
            && binding?.edEmail?.text?.isNotEmpty() == true
            && binding?.edLocation?.text?.isNotEmpty() == true
        ) {
            val user = User(
                userName = binding?.edUsername?.text.toString(),
                location = binding?.edLocation?.text.toString(),
                email = binding?.edEmail?.text.toString()
            )
            repo.insertUser(user)
            fetchUsers()
        } else {
            Toast.makeText(requireContext(), "Invalid Input", Toast.LENGTH_SHORT).show()
        }
    }


}