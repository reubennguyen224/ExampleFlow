package com.demoapp.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.demoapp.myapplication.data.Item
import com.demoapp.myapplication.data.getFormattedPrice
import com.demoapp.myapplication.databinding.FragmentItemDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.training.finalproject.utils.replaceFragment

class ItemDetailFragment : Fragment() {
    private val viewModel : InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }
    private var id: Int = 0
    lateinit var item: Item

    private var _binding : FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    companion object{
        fun newInstance(id: Int): ItemDetailFragment{
            val fragment = ItemDetailFragment()
            val bundle = Bundle()
            bundle.putInt("id_item", id)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getInt("id_item") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(item: Item){
        binding.apply {
            itemName.text = item.itemName
            itemPrice.text = item.getFormattedPrice()
            itemCount.text = item.quantityInStock.toString()
            sellItem.isEnabled = viewModel.isStockAvailable(item)
            sellItem.setOnClickListener { viewModel.sellItem(item) }
            deleteItem.setOnClickListener { showConfirmDialog() }
            editItem.setOnClickListener { editItem() }
        }
    }

    private fun editItem(){
        replaceFragment(AddItemFragment.newInstance(item.id), R.id.container, true)
    }

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(R.string.delete_question)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) {_, _ ->}
            .setPositiveButton(getString(R.string.yes)) {_, _ ->
                deleteItem()
            }.show()
    }

    private fun deleteItem(){
        viewModel.deleteItem(item)
        replaceFragment(ItemListFragment(), R.id.container, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveItem(id)
        viewModel.addItemState.observe(this.viewLifecycleOwner){
            bind(it.item)
            item = it.item
        }
    }
}