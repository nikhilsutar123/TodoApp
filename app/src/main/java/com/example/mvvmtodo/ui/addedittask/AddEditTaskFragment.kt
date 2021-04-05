package com.example.mvvmtodo.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codinginflow.mvvmtodo.R
import com.codinginflow.mvvmtodo.databinding.FragmentAddEditTaskBinding
import com.example.mvvmtodo.data.Task
import com.example.mvvmtodo.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            taskNameEditText.setText(viewModel.taskName)
            importantTaskCheckBox.isChecked = viewModel.taskImportance

            //checked immediately without animation
            importantTaskCheckBox.jumpDrawablesToCurrentState()
            dateCreatedTextView.isVisible = viewModel.task != null
            dateCreatedTextView.text = ("created: ${viewModel.task?.createDateFormatted}")

            taskNameEditText.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            importantTaskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect {event ->
                when (event) {
                   is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage ->{
                       Snackbar.make(requireView(),event.text,Snackbar.LENGTH_SHORT).show()
                   }
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateUpWithResult -> {
                        //hide the keyboard
                        binding.taskNameEditText.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result))
                        //Remove this fragment from the backstack and go to the previous fragment i.e TaskFragment
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}