package com.aboubakergb.mymemory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.aboubakergb.mymemory.models.BoardSize
import com.aboubakergb.mymemory.models.MemoryCard
import kotlin.math.min

class MemoryBoardAdapter(
    private var context: Context,
    private var boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener
)
    : RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object{
        private const val  MARGIN_SIZE=10
        private const val TAG ="MemoryBoardAdapter"
    }

    interface CardClickListener{
        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Fixed image button size and margin
        val cardWidth =parent.width / boardSize.getWidth()  - (2 * MARGIN_SIZE)
        val cardHeight =parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSlideLength = min(cardWidth,cardHeight)

        val view =LayoutInflater.from(context).inflate(R.layout.memory_card,parent,false)


        val layoutParams=view.findViewById<CardView>(R.id.card_View).layoutParams as ViewGroup.MarginLayoutParams
        // Fixed image button size and margin
        layoutParams.height=cardSlideLength
        layoutParams.width=cardSlideLength
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)

        return ViewHolder(view)
    }

    override fun getItemCount() = boardSize.numCards

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val imageButton =itemView.findViewById<ImageButton>(R.id.imageButton)
        
        fun bind(position: Int) {
            val memoryCard=cards[position]
            // set image card on default when start the game
            imageButton.setImageResource(if (cards[position].isFaceUp) cards[position].identifier else R.drawable.ic_launcher_background )
            // IF CARD IS MATCH SET IT FLOE
            imageButton.alpha=if (memoryCard.isMatched) .4f else 1.0f
            // IF CARD IS MATCH SET THE BACKGROUND GRAY
            val colorStateList =if(memoryCard.isMatched) ContextCompat.getColorStateList(context,R.color.color_gary) else null
            ViewCompat.setBackgroundTintList(imageButton,colorStateList)

            imageButton.setOnClickListener{
                Log.i(TAG ,"Clicked on position $position")
                cardClickListener.onCardClicked(position)
            }

        }
    }

}
