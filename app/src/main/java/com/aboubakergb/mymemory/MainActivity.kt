package com.aboubakergb.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import androidx.recyclerview.widget.GridLayoutManager
import com.aboubakergb.mymemory.models.BoardSize
import com.aboubakergb.mymemory.models.MemoryCard
import com.aboubakergb.mymemory.models.MemoryGame
import com.aboubakergb.mymemory.utils.DEFAULT_ICONS
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG="MainActivity"
    }

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame

    private val boardSize:BoardSize=BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         memoryGame =MemoryGame(boardSize)

        // set setting to recycle view
        adapter=MemoryBoardAdapter(this, boardSize, memoryGame.cards ,object :MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }
        })

        rv_Borad.adapter=adapter
        rv_Borad.setHasFixedSize(true)
        rv_Borad.layoutManager=GridLayoutManager(this,boardSize.getWidth())
    }

    private fun updateGameWithFlip(position: Int) {
        // Error checking
        if (memoryGame.haveWonGame()){
            // Alert the user of an invalid move
            Snackbar.make(clRoot,"You already won!",Snackbar.LENGTH_LONG).show()
            return
        }
        if (memoryGame.isCardFaceUP(position)){
            // Alert the user of an invalid move
            Snackbar.make(clRoot,"Invalid move!",Snackbar.LENGTH_LONG).show()
            return
        }
        memoryGame.flapCard(position)
        adapter.notifyDataSetChanged()
    }
}