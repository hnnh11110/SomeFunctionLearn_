package com.app.func

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.func.base_content.BaseActivity
import com.app.func.databinding.ActivityMultiThreadBinding

class MultiThreadActivity : BaseActivity() {

    private lateinit var binding: ActivityMultiThreadBinding
    private var mNavController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiThreadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavController()
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerViewSecond) as NavHostFragment
        mNavController = navHostFragment.navController
    }

    override fun getNavController(): NavController? = mNavController

}