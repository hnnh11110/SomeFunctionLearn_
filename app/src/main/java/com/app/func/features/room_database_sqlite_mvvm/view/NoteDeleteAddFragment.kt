package com.app.func.features.room_database_sqlite_mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.app.func.R
import com.app.func.base_content.BaseFragment
import com.app.func.databinding.NoteAddEditLayoutBinding
import com.app.func.features.room_database_sqlite_mvvm.Note
import com.app.func.features.room_database_sqlite_mvvm.utils.ConstantsNote
import com.app.func.utils.MyToast

class NoteDeleteAddFragment : BaseFragment() {

    private var binding: NoteAddEditLayoutBinding? = null
    private lateinit var mode: Mode
    private var noteId: Int = -1
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NoteAddEditLayoutBinding.inflate(inflater, container, false)
        binding?.numberPickerPriority?.minValue = 1
        binding?.numberPickerPriority?.maxValue = 10

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        noteId = arguments?.getInt(ConstantsNote.EXTRA_ID, -1) ?: -1
        mode = if (noteId == -1) {
            Mode.AddNote
        } else {
            Mode.EditNote
        }

        when (mode) {
            Mode.AddNote -> {
//                title = "Add Note"
            }
            Mode.EditNote -> {
//                title = "Edit Note"
                binding?.etTitle?.setText(arguments?.getString(ConstantsNote.EXTRA_TITLE))
                binding?.etDesc?.setText(arguments?.getString(ConstantsNote.EXTRA_DESCRIPTION))
                binding?.numberPickerPriority?.value =
                    arguments?.getInt(ConstantsNote.EXTRA_PRIORITY, -1) ?: 0
            }
        }

        binding?.btnSaveNote?.setOnClickListener {
            saveNote()
        }

        return binding?.root
    }

    private fun saveNote() {
        val title = binding?.etTitle?.text.toString()
        val desc = binding?.etDesc?.text.toString()
        val priority = binding?.numberPickerPriority?.value

        if (title.isEmpty() || desc.isEmpty()) {
            MyToast.showToast(requireContext(), "please insert title and description")
            return
        }

        val note = priority?.let { Note(title, desc, it) }
        if (mode == Mode.AddNote) {
            note?.let {
                noteViewModel.insert(it)
                MyToast.showToast(requireContext(), "Insert note.")
            }
        } else if (mode == Mode.EditNote) {
            note?.let {
                noteViewModel.update(it)
                MyToast.showToast(requireContext(), "Update note.")
            }
        }

//        getNavController()?.navigateUp()

        //val data = activity?.intent
        // only if note ID was provided i.e. we are editing
        /* if (noteId != -1) {
             data?.putExtra(ConstantsNote.EXTRA_ID, noteId)
             data?.putExtra(ConstantsNote.EXTRA_TITLE, title)
             data?.putExtra(ConstantsNote.EXTRA_DESCRIPTION, desc)
             data?.putExtra(ConstantsNote.EXTRA_PRIORITY, priority)
         }*/

//        setResult(Activity.RESULT_OK, data)
//        finish()
    }

    companion object {
        fun newFragment() = NoteDeleteAddFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private sealed class Mode {
        object AddNote : Mode()
        object EditNote : Mode()

    }
}