package com.aboubakergb.mymemory

import android.animation.ArgbEvaluator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.aboubakergb.mymemory.models.BoardSize
import com.aboubakergb.mymemory.models.MemoryCard
import com.aboubakergb.mymemory.models.MemoryGame
import com.aboubakergb.mymemory.utils.DEFAULT_ICONS
import com.aboubakergb.mymemory.utils.EXTRA_BOARD_SIZE
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_board_size.*

class MainActivity : AppCompatActivity() {



    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame

    private var boardSize:BoardSize=BoardSize.EASY

    companion object{
        // put any key you want (Int)
        private const val CREATE_REQUEST_CODE=111
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         setupBoard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.mi_Refresh->{
                if (memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?",null ,View.OnClickListener {
                        setupBoard()
                    })
                }else{
                setupBoard()
                }
                return true
            }

            R.id.mi_new_size->{
                showNewSizeDialog()
                return true
            }

            R.id.mi_costume->{
                showCreationDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // show dialog when select costume game
    private fun showCreationDialog() {
        val boardSizeView =LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val radioGroupSize=boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        showAlertDialog("Create your own memory board",boardSizeView,View.OnClickListener {
            // set a new value for the board size
            val desireBoardSize = when(radioGroupSize.checkedRadioButtonId){
                R.id.rdEasy-> BoardSize.EASY
                R.id.rdMediam->BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            // Navigation to a new Activity
            val intent=Intent(this,CreateActiviy::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE,desireBoardSize)
            startActivityForResult(intent,CREATE_REQUEST_CODE)
        })

    }

    // new dialog when select choose new size
    private fun showNewSizeDialog() {
        // show new dialog when we choose new size
        val boardSizeView =LayoutInflater.from(this).inflate(R.layout.dialog_board_size,null)
        val radioGroupSize=boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        // to set check automatically
        when(boardSize){
            BoardSize.EASY->radioGroupSize.check(R.id.rdEasy)
            BoardSize.MEDIUM->radioGroupSize.check(R.id.rdMediam)
            BoardSize.HARD ->radioGroupSize.check(R.id.rdHard)
        }

        showAlertDialog("Choose new Size",boardSizeView,View.OnClickListener {
            // set a new value for the board size
            boardSize = when(radioGroupSize.checkedRadioButtonId){
                R.id.rdEasy-> BoardSize.EASY
                R.id.rdMediam->BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setupBoard()
        })
    }

    // Alert Dialog showing when you want to refresh the game
    private fun showAlertDialog(title:String, view : View? , positiveClickListener: View.OnClickListener ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel",null)
            .setPositiveButton("OK"){_, _ ->
                positiveClickListener.onClick(null)

            }.show()
    }

    // this the main fun we have adapter
    fun setupBoard(){
        // when fun to change  the text in the numPairs and numMoves
        when(boardSize){
            BoardSize.EASY -> {
                tv_Num_Moves.text="EASY : 4 x 2 "
                tv_Num_Pairs.text="Pairs :0 / 4"
            }
            BoardSize.MEDIUM -> {
                tv_Num_Moves.text="MEDIUM : 6 x 3 "
                tv_Num_Pairs.text="Pairs :0 / 9"
            }
            BoardSize.HARD ->{
                tv_Num_Moves.text="HARD : 6 x 4 "
                tv_Num_Pairs.text="Pairs :0 / 12"
            }
        }
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
            Snackbar.make(clRoot,"Invalid move!",Snackbar.LENGTH_SHORT).show()
            return
        }
        //actually flip over the card
        if(memoryGame.flapCard(position)){
            var color =ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat(),
                ContextCompat.getColor(this, R.color.color_progress_none),
                ContextCompat.getColor(this, R.color.color_progress_full)
            ) as Int
            tv_Num_Pairs.setTextColor(color)
            tv_Num_Pairs.text="Pairs: ${memoryGame.numPairsFound}/ ${boardSize.getPairs()}"
            if(memoryGame.haveWonGame()){
                Snackbar.make(clRoot,"You won! Congratulations",Snackbar.LENGTH_LONG).show()
            }
        }
        tv_Num_Moves.text="Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }
}