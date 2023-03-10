package com.app.func.login_demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private val mCountChangeListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            binding?.pulsator?.count = progress + 1
            binding?.textCount?.text = String.format(Locale.US, "%d", progress + 1)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }

    private val mDurationChangeListener: OnSeekBarChangeListener =
        object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding?.pulsator?.duration = progress * 100
                binding?.textDuration?.text = String.format(
                    Locale.US, "%.1f", progress * 0.1f
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding?.btnViewProfile?.setOnClickListener {
            getNavController()?.navigate(R.id.snowyMainFragment)
        }

        binding?.btnSwipeToDelete?.setOnClickListener {
            getNavController()?.navigate(R.id.listUserFragment)
        }

        binding?.seekCount?.setOnSeekBarChangeListener(mCountChangeListener)
        binding?.seekCount?.progress = binding?.pulsator?.count?.minus(1)!!

        binding?.seekDuration?.setOnSeekBarChangeListener(mDurationChangeListener)
        binding?.seekDuration?.progress = binding?.pulsator?.duration?.div(100)!!
        initActions()

        return binding?.root
    }

    private fun initActions() {
        binding?.pulsator?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}