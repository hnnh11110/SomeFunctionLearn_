package com.app.func

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.func.base_content.BaseActivity
import com.app.func.databinding.ActivityViewAnimationsBinding
import java.util.*


class ViewAnimationsActivity : BaseActivity() {

    private lateinit var binding: ActivityViewAnimationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale("en")
        binding = ActivityViewAnimationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun getNavController(): NavController {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        return navHostFragment.navController
    }

    /*override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                if (fragment is MainContainFragment) {
                    //finish()
                } else {
                    //super.onBackPressed()
                }
            }
        }
    }*/

    // Pass "en","hi", etc.
    private fun setLocale(locale: String) {
        val myLocale = Locale(locale)
        // Saving selected locale to session - SharedPreferences.
        //saveLocale(locale)
        // Changing locale.
        Locale.setDefault(myLocale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(myLocale)
        } else {
            config.locale = myLocale
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            baseContext.createConfigurationContext(config)
        } else {
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }
}