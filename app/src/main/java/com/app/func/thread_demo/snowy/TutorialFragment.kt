package com.app.func.thread_demo.snowy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentTutorialBinding
import com.app.func.thread_demo.snowy.model.Tutorial
import com.app.func.thread_demo.snowy.utils.SnowFilter
import kotlinx.coroutines.*
import java.io.InputStream
import java.net.URL

class TutorialFragment : BaseFragment() {

    private var binding: FragmentTutorialBinding? = null
    private val parentJob = Job()

    // 1
    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            //2
            coroutineScope.launch(Dispatchers.Main) {
                //3
                binding?.errorMessage?.visibility = View.VISIBLE
                binding?.errorMessage?.text = getString(R.string.error_message)
            }

            coroutineScope.launch { println("Caught $throwable") }
//            GlobalScope.launch { println("Caught $throwable") }
        }

    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val tutorial: Tutorial? = arguments?.getParcelable(TUTORIAL_KEY) as? Tutorial
//        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)
        binding = FragmentTutorialBinding.inflate(inflater, container, false)
        binding?.tutorialName?.text = tutorial?.name
        binding?.tutorialDesc?.text = tutorial?.description
        return binding?.root
    }

    override fun setTitleActionBar() {
        //Remove title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tutorial = arguments?.getParcelable(TUTORIAL_KEY) as? Tutorial
        coroutineScope.launch(Dispatchers.Main) {
            val originalBitmap: Bitmap? = tutorial?.let { getOriginalBitmapAsync(it) }
            //1
            //val snowFilterBitmap: Bitmap? = originalBitmap?.let { loadSnowFilterAsync(it) }
            //2
            //snowFilterBitmap?.let { loadImage(it) }
            originalBitmap?.let { loadImage(it) }
        }
    }

    // 1
    private suspend fun getOriginalBitmapAsync(tutorial: Tutorial): Bitmap =
        withContext(Dispatchers.IO) {
            URL(tutorial.url).openStream().use {
                return@withContext BitmapFactory.decodeStream(it)
            }
        }

    fun getBitmapFromInternet(imageURL: String): Bitmap? {
        val `in`: InputStream = URL(imageURL).openStream()
        return BitmapFactory.decodeStream(`in`)
    }

    private suspend fun loadSnowFilterAsync(originalBitmap: Bitmap): Bitmap =
        withContext(Dispatchers.Default) {
            SnowFilter.applySnowEffect(originalBitmap)
        }

    private fun loadImage(snowFilterBitmap: Bitmap) {
        binding?.progressBar?.visibility = View.GONE
        binding?.snowFilterImage?.setImageBitmap(snowFilterBitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        parentJob.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val TUTORIAL_KEY = "TUTORIAL"

        fun newInstance(tutorial: Tutorial): TutorialFragment {
            val fragmentHome = TutorialFragment()
            val args = Bundle()
            args.putParcelable(TUTORIAL_KEY, tutorial)
            fragmentHome.arguments = args
            return fragmentHome
        }
    }
}