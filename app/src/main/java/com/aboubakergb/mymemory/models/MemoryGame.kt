package com.aboubakergb.mymemory.models

import com.aboubakergb.mymemory.utils.DEFAULT_ICONS

class MemoryGame (private val boardSize: BoardSize){

    val cards:List<MemoryCard>
    var numPairsFound =0

    private var indexOfSingleSelectedCard:Int?=null

    init {
        // randomized images
        val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getPairs())
        val randomizedImages =(chosenImages +chosenImages).shuffled()
        cards =randomizedImages.map{MemoryCard(it)}
    }

    fun flapCard(position: Int):Boolean {

        var foundMatch=false

        val card =cards[position]
        if (indexOfSingleSelectedCard==null){
            restoreCards()
            indexOfSingleSelectedCard=position
        }else {
            val foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp =!card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Any {
        // if the card has not  the same picture in  the second card then set the default state
        if (cards[position1].identifier != cards[position2].identifier){
            return false
        }
        // else that means card one has the same picture in the second picture then matched is true
        cards[position1].isMatched=true
        cards[position2].isMatched=true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        // if card is not matched - back to default state
        for (card in cards){
            if(!card.isMatched){
                card.isFaceUp=false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound==boardSize.getPairs()
    }

    fun isCardFaceUP(position: Int): Boolean {
        return cards[position].isFaceUp
    }
}