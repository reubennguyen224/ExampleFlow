package com.demoapp.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.demoapp.myapplication.data.Item
import com.demoapp.myapplication.data.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InventoryViewModel(private val itemDao: ItemDao): ViewModel() {
    val listItemState = stateFlow(ItemListState())
    val addItemState = stateFlow(AddItemState())

    init {
        viewModelScope.launch(Dispatchers.IO){
            itemDao.getItems().collect{
                listItemState.value.copy(
                    listItem = it,
                )
            }
        }
    }

    fun isStockAvailable(item: Item) : Boolean {
        return (item.quantityInStock > 0)
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

    private fun updateItem(item: Item){
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    private fun getUpdatedItemEntry(itemId: Int, itemName: String, itemPrice: String, itemCount: String): Item {
        val str = itemPrice.replace(",", ".")
        return Item(id = itemId, itemName = itemName, itemPrice = str.toDouble(), quantityInStock = itemCount.toInt())
    }

    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        val str = itemPrice.replace(",", ".")
        return Item(itemName = itemName, itemPrice = str.toDouble(), quantityInStock = itemCount.toInt())
    }

    fun sellItem(item: Item){
        if (item.quantityInStock > 0){
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String) : Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) return false
        return true
    }

    fun retrieveItem(id: Int){
        viewModelScope.launch {
            itemDao.getItem(id = id).collect{
                addItemState.value.copy(
                    item = it
                )
            }
        }
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount:String){
        val newItem = getNewItemEntry(itemName = itemName, itemPrice = itemPrice, itemCount)
        insertItem(newItem)
    }

    fun deleteItem(item: Item){
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    private fun insertItem(item: Item){
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }
}

data class ItemListState(
    val listItem: List<Item> = emptyList(),
)

data class AddItemState(
    val item: Item = Item()
)

@Suppress("UNCHECKED_CAST")
class InventoryViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)){
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}