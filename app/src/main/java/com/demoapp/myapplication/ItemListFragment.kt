package com.demoapp.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demoapp.myapplication.databinding.ItemListFragmentBinding
import com.training.finalproject.utils.replaceFragment

class ItemListFragment : Fragment() {

    private val viewModel : InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.floatingActionButton.setOnClickListener {
            replaceFragment(AddItemFragment.newInstance(0), R.id.container, true)
        }
        val adapter = ItemListAdapter {
            replaceFragment(ItemDetailFragment.newInstance(it.id), R.id.container, true)
        }
        binding.recyclerView.adapter = adapter


        viewModel.listItemState.observe(this.viewLifecycleOwner) {
            adapter.submitList(it.listItem)
            //Log
        }
        //Log
    }
}