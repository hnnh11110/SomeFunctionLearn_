package com.app.func.login_demo

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.coroutine_demo.retrofit.aaa.MyViewModelFactory
import com.app.func.coroutine_demo.retrofit.base.ApiConstants
import com.app.func.databinding.FragmentSignUpBinding
import com.app.func.view.recycler_view_custom.ravi_recyclerview.CartListAdapter
import com.app.func.view.recycler_view_custom.ravi_recyclerview.CartListAdapter.MyViewHolder
import com.app.func.view.recycler_view_custom.ravi_recyclerview.ItemCart
import com.app.func.view.recycler_view_custom.ravi_recyclerview.RecyclerItemTouchHelper
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : BaseFragment(), View.OnClickListener,
    RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private var binding: FragmentSignUpBinding? = null
    private val cartList = mutableListOf<ItemCart>()
    private var mAdapter = CartListAdapter()
    private var mViewModel: SignUpViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        /*mViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(getRepositoryRetrofit(ApiConstants.BASE_URL_FOOD))
        )[SignUpViewModel::class.java]

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding?.recyclerView?.layoutManager = mLayoutManager
        binding?.recyclerView?.itemAnimator = DefaultItemAnimator()
        binding?.recyclerView?.addItemDecoration(dividerItemDecoration)
        binding?.recyclerView?.adapter = mAdapter

        mViewModel?.getListMenuFood()

        initObservers()
        mViewModel?.loading?.observe(viewLifecycleOwner) {
            if (it) {
                //mBinding?.progressBar?.visibility = View.VISIBLE
            } else {
               // mBinding?.progressBar?.visibility = View.GONE
            }
        }



        *//* adding item touch helper
         only ItemTouchHelper.LEFT added to detect Right to Left swipe
         if you want both Right -> Left and Left -> Right
         add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param*//*
        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding?.recyclerView)

        //prepareCart()
        val itemTouchHelperCallback1 = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }*/

        //attaching the touch helper to recycler view
        //ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(binding?.recyclerView)

        //binding?.btnCreateAccount?.setOnClickListener(this)
        return binding?.root
    }

    private fun initObservers() {
        /*mViewModel?.menuFoodList?.observe(viewLifecycleOwner) {
           mAdapter.setDataAdapter(it)
        }*/

        observerError()
    }

    private fun observerError() {
        mViewModel?.errorMessage?.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }
    }

//    private fun prepareCart() {
//        cartList.add(ItemCart(1, "Item 1", "description 1", 123.0, "aaaa"))
//        cartList.add(ItemCart(2, "Item 2", "description 2", 456.0, "aaaa"))
//        cartList.add(ItemCart(3, "Item 3", "description 3", 789.0, "aaaa"))
//        cartList.add(ItemCart(4, "Item 4", "description 4", 112.0, "aaaa"))
//        cartList.add(ItemCart(5, "Item 5", "description 5", 345.0, "aaaa"))
//        cartList.add(ItemCart(6, "Item 6", "description 6", 678.0, "aaaa"))
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onClick(view: View?) {
        when (view) {
            binding?.btnCreateAccount -> {
                getNavController()?.navigate(R.id.profileFragment)
            }
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is MyViewHolder) {
            // get the removed item name to display it in snack bar
            val name = cartList[viewHolder.getAdapterPosition()].name
            // backup of removed item for undo purpose
            val deletedItem = cartList[viewHolder.getAdapterPosition()]
            val deletedIndex = viewHolder.getAdapterPosition()

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition())

            // showing snack bar with Undo option
            val snackbar = binding?.scrollViewSignUp?.let {
                Snackbar.make(it, "$name removed from cart!", Snackbar.LENGTH_LONG)
            }
            snackbar?.setAction("Undo") { // undo is selected, restore the deleted item
                mAdapter.restoreItem(deletedItem, deletedIndex)
            }
            snackbar?.setActionTextColor(Color.YELLOW)
            snackbar?.show()
        }
    }
}