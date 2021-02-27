package com.aboubakergb.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.aboubakergb.mymemory.models.BoardSize
import com.aboubakergb.mymemory.utils.DEFAULT_ICONS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val boardSize:BoardSize=BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // randomized images
        val chosenImages =DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
        val randomizedImages =(chosenImages +chosenImages).shuffled()
        // set setting to recycle view
        rv_Borad.adapter=MemoryBoardAdapter(this, boardSize, randomizedImages)
        rv_Borad.setHasFixedSize(true)
        rv_Borad.layoutManager=GridLayoutManager(this,boardSize.getWidth())
    }
}