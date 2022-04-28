package controllers

import models.Boxer
import persistence.Serializer
import utils.Validation.isValidListIndex

class BoxerAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var boxers = ArrayList<Boxer>()



    //  crud methods

    fun add(boxer: Boxer): Boolean {
        return boxers.add(boxer)
    }

    fun deleteBoxer(indexToDelete: Int): Boxer? {
        return if (isValidListIndex(indexToDelete, boxers)) {
            boxers.removeAt(indexToDelete)
        } else null
    }

    fun updateBoxer(indexToUpdate: Int, boxer: Boxer?): Boolean {
        //find the boxer object by the index number
        val foundBoxer = findBoxer(indexToUpdate)

        //if the note exists, use the boxer's details passed as parameters to update the found boxer in the ArrayList.
        if ((foundBoxer != null) && (boxer != null)) {
            foundBoxer.boxerName = boxer.boxerName
            foundBoxer.boxerWins = boxer.boxerWins
            foundBoxer.boxerClass = boxer.boxerClass
            return true
        }

        //if the boxer was not found, return false, indicating that the update was not successful
        return false
    }

    fun archiveBoxer(indexToArchive: Int): Boolean {
        if (isValidIndex(indexToArchive)) {
            val boxerToArchive = boxers[indexToArchive]
            if (!boxerToArchive.isBoxerArchived) {
                boxerToArchive.isBoxerArchived = true
                return true
            }
        }
        return false
    }


    //  listing methods

    fun listAllBoxers(): String =
        if (boxers.isEmpty()) "No boxers stored"
        else formatListString(boxers)

    fun listActiveBoxers(): String =
        if (numberOfActiveBoxers() == 0) "No active boxers stored"
        else

            formatListString(boxers.filter { boxer -> !boxer.isBoxerArchived })



    fun listArchivedBoxers(): String =
        if (numberOfArchivedBoxers() == 0) "No archived boxers stored"
        else

            formatListString(boxers.filter { boxer -> boxer.isBoxerArchived})


    fun listBoxersBySelectedWins(wins: Int): String {
        return if (boxers.isEmpty()) {
            "No boxers stored"
        } else {
            var listOfBoxers = ""
            for (i in boxers.indices) {
                if (boxers[i].boxerWins == wins) {
                    listOfBoxers +=
                        """$i: ${boxers[i]}
                        """.trimIndent()
                }
            }
            if (listOfBoxers.equals("")) {
                "No notes with priority: $wins"
            } else {
                "${numberOfBoxersByWins(wins)} notes with priority $wins: $listOfBoxers"
            }
        }
    }


    //  counting methods

    fun numberOfBoxers(): Int {
        return boxers.size
    }

    fun numberOfArchivedBoxers(): Int = boxers.count { boxer: Boxer -> boxer.isBoxerArchived }

    fun numberOfActiveBoxers(): Int = boxers.count { boxer: Boxer -> !boxer.isBoxerArchived }

    fun numberOfBoxersByWins(wins: Int): Int =
        boxers.count({it.boxerWins==wins})



    //  searching methods

    fun findBoxer(index: Int): Boxer? {
        return if (isValidListIndex(index, boxers)) {
            boxers[index]
        } else null
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, boxers);
    }

    fun searchByName (searchString : String):String {

        return formatListString(boxers.filter { boxer -> boxer.boxerName.contains(searchString, ignoreCase = true) })


    }


    //  helper methods



    fun formatListString(boxersToFormat : List<Boxer>) : String =
        boxersToFormat
            .joinToString (separator = "\n") { boxer ->
                boxers.indexOf(boxer).toString() + ": " + boxer.toString() }


    //  persistence methods

    @Throws(Exception::class)
    fun load() {
        boxers = serializer.read() as ArrayList<Boxer>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(boxers)
    }

    fun firstBoxer() : Boxer {
        return boxers.first()
    }

}

