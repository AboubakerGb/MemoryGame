package com.aboubakergb.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.aboubakergb.mymemory.models.BoardSize
import com.aboubakergb.mymemory.utils.EXTRA_BOARD_SIZE

class CreateActiviy : AppCompatActivity() {

    private lateinit var boardSize: BoardSize
    private var numImageRequired=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_activiy)
         // et back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        boardSize = intent.getSerializableExtra(EXTRA_BOARD_SIZE) as BoardSize
        numImageRequired=boardSize.getPairs()
        supportActionBar?.title="Choose pics ( 0/ $numImageRequired)"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== android.R.id.home){
            finish()
        return true
        }
        return super.onOptionsItemSelected(item)
    }
}