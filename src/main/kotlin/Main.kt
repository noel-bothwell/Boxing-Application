import controllers.BoxerAPI
import models.Boxer
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.ClassValidation.isValidClass
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.Validation.validRange
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}


private val boxerAPI = BoxerAPI(JSONSerializer(File("boxers.json")))

fun main(args: Array<String>) {
    runMenu()
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addBoxer()
            2 -> listBoxers()
            3 -> updateBoxer()
            4 -> deleteBoxer()
            5 -> archiveBoxer()
            6 -> numberOfBoxers()
            7 -> searchBoxers()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> System.out.println("Invalid option entered: $option")
        }
    } while (true)
}

fun mainMenu(): Int {
    return readNextInt(
        """ 
         > ----------------------------------
         > |        BOXER APP                |
         > ----------------------------------
         > | BOXER MENU                      |
         > |   1) Add boxer                  |
         > |   2) List boxers                |
         > |   3) Update a boxer             |
         > |   4) Delete a boxer             |
         > |   5) Archive a boxer            |
         > |   6) number of boxers           |
         > |---------------------------------|
         > |   7) Search boxers              |
         > |---------------------------------|
         > |   20) Save boxers               |
         > |   21) Load boxers               |
         > |   0)  Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}

fun addBoxer() {

    val boxerName = readNextLine("Enter a name for the boxer: ")

    var boxerWins: Int
    do {
        boxerWins = readNextInt("Enter the number of wins (1, 2, 3, 4, 5, 6, 7, 8, 9, 10): ")
    }
    while (!validRange(boxerWins,1,10))

    var boxerClass: String
    do {
        boxerClass = readNextLine("Enter a weight class for the boxer: ")
    }
    while (!isValidClass(boxerClass))

    val isAdded = boxerAPI.add(Boxer(boxerName, boxerWins, boxerClass, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBoxers() {
    if (boxerAPI.numberOfTotalBoxers() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL boxers          |
                  > |   2) View ACTIVE boxers       |
                  > |   3) View ARCHIVED boxers     |
                  > |   4) View FIRST boxer         |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBoxers()
            2 -> listActiveBoxers()
            3 -> listArchivedBoxers()
            4 -> firstBoxer()
            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option Invalid - No boxers stored")
    }
}

fun numberOfBoxers() {
    if (boxerAPI.numberOfTotalBoxers() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) Number of total boxers   |
                  > |   2) Number of Active boxers  |
                  > |   3) Number of Archived boxers|
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> numberOfTotalBoxers()
            2 -> numberOfActiveBoxers()
            3 -> numberOfArchivedBoxers()
            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option Invalid - No boxers stored")
    }
}

fun numberOfTotalBoxers() {
    println(boxerAPI.numberOfTotalBoxers())
}

fun numberOfActiveBoxers() {
    println(boxerAPI.numberOfActiveBoxers())
}

fun numberOfArchivedBoxers() {
    println(boxerAPI.numberOfArchivedBoxers())
}


fun firstBoxer() {
    println(boxerAPI.firstBoxer())
}


fun listAllBoxers() {
    println(boxerAPI.listAllBoxers())
}

fun listActiveBoxers() {
    println(boxerAPI.listActiveBoxers())
}

fun listArchivedBoxers() {
    println(boxerAPI.listArchivedBoxers())
}



fun updateBoxer() {

    listBoxers()
    if (boxerAPI.numberOfTotalBoxers() > 0) {
        //only ask the user to choose the boxer if boxers exist
        val indexToUpdate = readNextInt("Enter the index of the boxer to update: ")
        if (boxerAPI.isValidIndex(indexToUpdate)) {
            val boxername = readNextLine("Enter the name of the boxer: ")

            var boxerWins: Int
            do {
                boxerWins = readNextInt("Enter the number of wins (1, 2, 3, 4, 5, 6, 7, 8, 9, 10): ")
            }
                while (!validRange(boxerWins,1,10))

            var boxerClass: String
            do {
                boxerClass = readNextLine("Enter a weight class for the boxer: ")
            }
            while (!isValidClass(boxerClass))

            //pass the index of the boxer and the new boxer's details to BoxerAPI for updating and check for success.
            if (boxerAPI.updateBoxer(indexToUpdate, Boxer(boxername, boxerWins, boxerClass, false))) {
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun deleteBoxer() {

    listBoxers()
    if (boxerAPI.numberOfTotalBoxers() > 0) {
        //only ask the user to choose the boxer to delete if boxers exist
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        //pass the index of the boxer to BoxerAPI for deleting and check for success.
        val boxerToDelete = boxerAPI.deleteBoxer(indexToDelete)
        if (boxerToDelete != null) {
            println("Delete Successful! Deleted note: ${boxerToDelete.boxerName}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun archiveBoxer() {
    listActiveBoxers()
    if (boxerAPI.numberOfActiveBoxers() > 0) {
        //only ask the user to choose the boxer to archive if active boxers exist
        val indexToArchive = readNextInt("Enter the index of the boxer to archive: ")
        //pass the index of the note to BoxerAPI for archiving and check for success.
        if (boxerAPI.archiveBoxer(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun searchBoxers() {
    val searchName = readNextLine("Enter name to search: ")
    val searchResults = boxerAPI.searchByName(searchName)
    if (searchResults.isEmpty()) {
        println("No boxers were found")
    } else {
        println(searchResults)
    }
}



fun save() {
    try {
        boxerAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        boxerAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp() {
    println("Exiting..bye..Thank You..")
    exit(0)
}