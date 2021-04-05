package com.example.mvvmtodo.ui.deleteAllCompleted

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class deleteAllCompletedFragment : DialogFragment() {

    private val viewModel: deleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Do you want to delete all completed tasks?")
            .setNegativeButton("cancel", null)
            .setPositiveButton("yes") { _, _ ->
                viewModel.onConfirmClick()
            }.create()
}