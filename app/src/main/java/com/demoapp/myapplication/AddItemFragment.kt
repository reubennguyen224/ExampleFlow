package com.demoapp.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.demoapp.myapplication.data.Item
import com.demoapp.myapplication.databinding.FragmentAddItemBinding
import com.training.finalproject.utils.popBackStackInclusive
import com.training.finalproject.utils.replaceFragment

class AddItemFragment : Fragment(){
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel : InventoryViewModel by activityViewModels{
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    lateinit var item: Item
    private var id: Int = 0

    companion object{
        fun newInstance(id: Int): AddItemFragment{
            val bundle = Bundle()
            bundle.putInt("id_item", id)
            val fragment = AddItemFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getInt("id_item") ?: 0
    }

    private fun isEntryValid(): Boolean{
        return viewModel.isEntryValid(binding.itemName.text.toString(), binding.itemPrice.text.toString(), binding.itemCount.text.toString())
    }

    private fun bind(item: Item){
        val price = "%.2f".format(item.itemPrice)
        binding.apply {
            itemName.setText(item.itemName, TextView.BufferType.SPANNABLE)
            itemPrice.setText(price, TextView.BufferType.SPANNABLE)
            itemCount.setText(item.quantityInStock.toString(), TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateItem() }
        }
    }

    private fun updateItem() {
        if (isEntryValid()){
            viewModel.updateItem(
                id,
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString()
            )
            popBackStackInclusive()
        }
    }

    private fun addNewItem(){
        if (isEntryValid()){
            viewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString()
            )
            replaceFragment(ItemListFragment(), R.id.container, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveItem(id)
        if (id > 0) {
            viewModel.addItemState.observe(this.viewLifecycleOwner) {
                bind(it.item)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewItem()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}