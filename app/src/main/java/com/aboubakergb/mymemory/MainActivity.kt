package com.aboubakergb.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_Borad.adapter=MemoryBoardAdapter(this, 8)
        rv_Borad.setHasFixedSize(true)
        rv_Borad.layoutManager=GridLayoutManager(this,2)
    }
}