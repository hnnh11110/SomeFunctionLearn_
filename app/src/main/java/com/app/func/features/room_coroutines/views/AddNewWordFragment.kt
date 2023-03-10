package com.app.func.features.room_coroutines.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.app.func.base_content.BaseFragment
import com.app.func.base_content.WordsApplication
import com.app.func.databinding.AddNewWordFragmentBinding
import com.app.func.features.room_coroutines.Word
import com.app.func.features.room_coroutines.WordViewModel
import com.app.func.features.room_coroutines.WordViewModelFactory
import com.app.func.utils.MyToast

class AddNewWordFragment : BaseFragment() {

    private var binding: AddNewWordFragmentBinding? = null
    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((activity?.application as WordsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddNewWordFragmentBinding.inflate(inflater, container, false)
        binding?.buttonSave?.setOnClickListener {
            val word: String = binding?.editWord?.text.toString().trim()
            if (word.isNotBlank() && word.isNotEmpty()) {
                wordViewModel.insert(Word(word = word))
            } else {
                MyToast.showToast(requireContext(), "Input in valid")
            }
        }
        return binding?.root
    }

    companion object {
        fun newFragment() = AddNewWordFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

