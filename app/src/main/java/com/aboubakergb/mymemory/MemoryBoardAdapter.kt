package com.aboubakergb.mymemory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class MemoryBoardAdapter( private var context: Context, private var numPieces: Int) : RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object{
        private const val  MARGIN_SIZE=10
        private const val TAG ="MemoryBoardAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Fixed image button size and margin
        val cardWidth =parent.width / 2  - (2 * MARGIN_SIZE)
        val cardHeight =parent.height / 4 - (2 * MARGIN_SIZE)
        val cardSlideLength = min(cardWidth,cardHeight)

        val view =LayoutInflater.from(context).inflate(R.layout.memory_card,parent,false)


        val layoutParams=view.findViewById<CardView>(R.id.card_View).layoutParams as ViewGroup.MarginLayoutParams
        // Fixed image button size and margin
        layoutParams.height=cardSlideLength
        layoutParams.width=cardSlideLength

        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun getItemCount() = numPieces

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val imageButton =itemView.findViewById<ImageButton>(R.id.imageButton)
        
        fun bind(position: Int) {
            imageButton.setOnClickListener{
                Log.i(TAG ,"Clicked on position $position")
            }

        }
    }

}
