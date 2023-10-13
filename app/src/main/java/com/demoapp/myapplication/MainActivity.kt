package com.demoapp.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.demoapp.myapplication.databinding.ActivityMainBinding
import com.training.finalproject.utils.replaceFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ItemListFragment(), R.id.container, true)


        //flow

        //edit text1
        //edit text2

        //ca 2 edittext deu phai co data

        //flow1.combine(flow2).collect{ string1, string2
        //
        // }.map{
        //  //an
        //hien
        // }.collect{
        //  button.enbale =
        // }

        //thi enable button

        //


//        val shareFlow = MutableSharedFlow<Int>(extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST, replay = 1)


//        GlobalScope.launch {
//            shareFlow.emit(10)
//            Log.d("LinhBD", "Test")
//        }
//
//        GlobalScope.launch {
//            shareFlow.collect {
//
//            }
//
//            Log.d("LinhBD", "TEST")
//        }
//
//        val stateFlow = MutableStateFlow(10)
//
//        for(i in 0..100){
//            stateFlow.emit(i)
//        }
//
//
//        stateFlow.collect{
//            //12 se duoc goi o day
//            //chi goi gia tri cuoi
//        }

        //khong phat lai gia tri trung lap
        //se co co che debounce mac dinh   // debounce // tinh nang search
        //

        //thay the liveData, google, flow thay the thang liveData
        //them rat nhieu toan tu
        //
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1){
            finish()
        } else
        super.onBackPressed()
    }
}

