package com.app.func.features.room_coroutines.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.base_content.WordsApplication
import com.app.func.databinding.NoteHomeLayoutBinding
import com.app.func.features.room_coroutines.WordViewModel
import com.app.func.features.room_coroutines.WordViewModelFactory
import com.app.func.features.room_coroutines.adapters.WordListAdapter

class MainRoomCoroutinesFragment : BaseFragment() {

    private var binding: NoteHomeLayoutBinding? = null
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((activity?.application as WordsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteHomeLayoutBinding.inflate(inflater, container, false)
        binding?.buttonAddNote?.setOnClickListener {
            getNavController()?.navigate(R.id.addNewWordFragment)
        }

        val wordListAdapter = WordListAdapter {

        }
        binding?.recyclerView?.adapter = wordListAdapter
        binding?.recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        wordViewModel.allWords.observe(viewLifecycleOwner) {
            it.let { wordListAdapter.submitList(it) }
        }
        return binding?.root
    }

    companion object {
        fun newFragment() = MainRoomCoroutinesFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

