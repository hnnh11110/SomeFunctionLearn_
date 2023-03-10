package com.app.func.view

import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentListUserBinding
import com.app.func.utils.MyToast
import com.app.func.view.recycler_view_custom.ItemTouchHelperListener
import com.app.func.view.recycler_view_custom.RecyclerViewItemTouchHelper
import com.app.func.view.recycler_view_custom.SwipeHelper
import com.app.func.view.recycler_view_custom.adapters.UserAdapter
import com.app.func.view.recycler_view_custom.adapters.UserAdapter2
import com.app.func.view.recycler_view_custom.models.User
import com.google.android.material.snackbar.Snackbar

class ListUserFragment : BaseFragment() {

    private var bindingListUserFragment: FragmentListUserBinding? = null
    private var mUsers = mutableListOf<User>()
    private var mUsers2 = mutableListOf<User>()
    private var userAdapter = UserAdapter()
    private var userAdapter2 = UserAdapter2()
    private val listenerSwipeAction = object : ItemTouchHelperListener {
        override fun onSwipedViewToLeft(viewHolder: RecyclerView.ViewHolder) {
            actionSwipedViewToLeft(viewHolder)
        }

        override fun onSwipedViewToRight(viewHolder: RecyclerView.ViewHolder) {
            actionSwipedViewToRight(viewHolder)
        }
    }

    //RecyclerView2
    private val simpleItemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    userAdapter2.removeUser(position)
                    //userAdapter2.notifyItemChanged(position)
                    MyToast.showToast(requireContext(), "Swipe left")
                } else if (direction == ItemTouchHelper.RIGHT) {
                    userAdapter2.removeUser(position)
                    //userAdapter2.notifyItemChanged(position)
                    MyToast.showToast(requireContext(), "Swipe right")
                }
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
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    if (dX < 0) {
                        val paint = Paint()
                        paint.color = Color.RED
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, paint)
                        val icon = BitmapFactory.decodeResource(
                            requireActivity().resources,
                            R.drawable.ic_white_delete
                        )
                        val margin = (dX / 5 - width) / 2
                        val iconDest = RectF(
                            itemView.right.toFloat() + margin,
                            itemView.top.toFloat() + width,
                            itemView.right.toFloat() + (margin + width),
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, iconDest, paint)
                    }
                    if (dX > 0) {
                        val paint = Paint()
                        paint.color = Color.BLUE
                        val background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.left.toFloat() + dX,
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, paint)
                        val icon = BitmapFactory.decodeResource(
                            requireActivity().resources,
                            R.drawable.ic_baseline_checked
                        )
                        val margin = (dX / 5 - width) / 2
                        val iconDest = RectF(
                            margin,
                            itemView.top.toFloat() + width,
                            margin + width,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, iconDest, paint)
                    }
                } else {
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX / 5,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingListUserFragment = FragmentListUserBinding.inflate(inflater, container, false)

        mUsers = getListUser()
        userAdapter.initData(mUsers)
        bindingListUserFragment?.recyclerViewUser?.adapter = userAdapter
        val liner = LinearLayoutManager(requireContext())
        bindingListUserFragment?.recyclerViewUser?.layoutManager = liner
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        bindingListUserFragment?.recyclerViewUser?.addItemDecoration(dividerItemDecoration)
        val simpleCallback =
            RecyclerViewItemTouchHelper(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
                listenerSwipeAction
            )
        ItemTouchHelper(simpleCallback).attachToRecyclerView(bindingListUserFragment?.recyclerViewUser)

        //Recyclerview2
        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        mUsers2 = getListUser()
        userAdapter2.initData(mUsers2)
        bindingListUserFragment?.recyclerViewUser2?.adapter = userAdapter2
        bindingListUserFragment?.recyclerViewUser2?.layoutManager = layoutManager
        //bindingListUserFragment?.recyclerViewUser2?.addItemDecoration(itemDecoration)
        //val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        //itemTouchHelper.attachToRecyclerView(bindingListUserFragment?.recyclerViewUser2)

        val itemTouchHelper = object : SwipeHelper(context) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(
                    UnderlayButton(
                        requireContext(),
                        getString(R.string.button_delete),
                        R.drawable.ic_white_delete,
                        ContextCompat.getColor(requireContext(), R.color.color_E93333),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
//                                checkNoInternetWrapper {
//                                    viewModel?.onDeleteSchedule(adapter.getData()[pos])
//                                }
                            }
                        }
                    )
                )
            }
        }
        itemTouchHelper.attachToRecyclerView(bindingListUserFragment?.recyclerViewUser2)

        return bindingListUserFragment?.root
    }

    private fun getListUser(): MutableList<User> {
        val listUser = mutableListOf<User>()
        for (i in 0 until 20) {
            listUser.add(User(id = i, name = "User ${i + 1}"))
        }
        return listUser
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bindingListUserFragment = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = ListUserFragment()
    }


    private fun actionSwipedViewToLeft(viewHolder: RecyclerView.ViewHolder) {
        MyToast.showToast(requireContext(), "${viewHolder.adapterPosition}")
        if (viewHolder is UserAdapter.UserViewHolder) {
            val indexDelete = viewHolder.adapterPosition
            val userRemove = mUsers[indexDelete]
            val nameRemove = userRemove.name
            userAdapter.removeUser(indexDelete)

            val notify = "You remove: $nameRemove at position: ${indexDelete + 1}"
            val snackbar = bindingListUserFragment?.consRootView?.let {
                Snackbar.make(it, notify, Snackbar.LENGTH_LONG)
            }
            snackbar?.setAction("Undo") {
                userAdapter.undoRemoveUser(indexDelete, userRemove)
                if (indexDelete == 0 || indexDelete == mUsers.size - 1) {
                    bindingListUserFragment?.recyclerViewUser?.scrollToPosition(indexDelete)
                }
            }
            snackbar?.setActionTextColor(Color.RED)
            snackbar?.show()
        }
    }

    private fun actionSwipedViewToRight(viewHolder: RecyclerView.ViewHolder) {
        MyToast.showToast(requireContext(), "Swipe to right.! --- ${viewHolder.adapterPosition}")
    }
}