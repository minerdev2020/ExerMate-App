package com.minerdev.exermate.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.minerdev.exermate.databinding.DialogDropdownBinding

class DropdownDialog(
    private val items: List<String>,
    private val listener: AdapterView.OnItemClickListener
) : BaseDialogFragment() {
    private val binding by lazy { DialogDropdownBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.listView.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            listener.onItemClick(parent, view, position, id)
            dismiss()
        }

        return binding.root
    }
}