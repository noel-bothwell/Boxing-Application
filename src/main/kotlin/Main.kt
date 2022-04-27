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
            6 -> firstBoxer()
            7 -> listArchivedBoxers()
            8 -> numberOfBoxers()
            9 -> searchBoxers()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> System.out.println("Invalid option entered: ${option}")
        }
    } while (true)
}

fun mainMenu(): Int {
    return readNextInt(
        """ 
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add Boxer                  |
         > |   2) List Boxers                |
         > |   3) Update a Boxer            |
         > |   4) Delete a Boxer             |
         > |   5) Archive a Boxer            |
         > |   6) List first Boxer           |
         > |   7) List archived Boxers       |
         > |   8) number of Boxers           |
         > |--------------------------------|
         > |   9) Search Boxers              |
         > |--------------------------------|
         > |   20) Save Boxers               |
         > |   21) Load Boxers               |
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}

fun addBoxer() {
    //logger.info { "addNote() function invoked" }
    val boxerName = readNextLine("Enter a name for the Boxer: ")
    var boxerWins = readNextInt("Enter number of wins (1, 2, 3, 4, 5, 6, 7, 8, 9, 10): ")
    if (!validRange(boxerWins,1,10)){BoxerWins = readNextInt("Enter number of wins (1, 2, 3, 4, 5, 6, 7, 8, 9, 10): ") }

    var boxerClass = readNextLine("Enter a category for the Boxer: ")
    if (!isValidClass(boxerClass)){boxerClass = readNextLine("Enter a weight class for the Boxer: ")}

    val isAdded = boxerAPI.add(Boxer(boxerName, boxerWins, boxerClass, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBoxers() {
    if (boxerAPI.numberOfBoxers() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL Boxers          |
                  > |   2) View ACTIVE Boxers       |
                  > |   3) View ARCHIVED Boxers     |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBoxers();
            2 -> listActiveBoxers();
            3 -> listArchivedBoxers();
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No Boxers stored");
    }
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

fun numberOfBoxers() {
    println(boxerAPI.numberOfBoxers())
}

fun updateBoxer() {

    listBoxers()
    if (boxerAPI.numberOfBoxers() > 0) {
        //only ask the user to choose the Boxer if Boxers exist
        val indexToUpdate = readNextInt("Enter the index of the Boxer to update: ")
        if (boxerAPI.isValidIndex(indexToUpdate)) {
            val boxerName = readNextLine("Enter a name for the Boxer: ")
            var boxerWins = readNextInt("Enter number of wins (1, 2, 3, 4, 5, 6, 7, 8, 9, 10): ")
            if (!validRange(boxerWins,1,10)){boxerWins = readNextInt("Enter number of wins (1, 2, 3, 4, 5, 6, 7, 8, 9, 10): ") }

            var boxerClass = readNextLine("Enter a weight class for the Boxer: ")
            if (!isValidClass(BoxerClass)){boxerClass = readNextLine("Enter a weight class for the Boxer: ")}

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (boxerAPI.updateNote(indexToUpdate, Boxer(boxerName, boxerWins, boxerClass, false))) {
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
    //logger.info { "deleteNotes() function invoked" }
    listBoxers()
    if (boxerAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note to delete if notes exist
        val indexToDelete = readNextInt("Enter the index of the boxer to delete: ")
        //pass the index of the note to NoteAPI for deleting and check for success.
        val boxerToDelete = boxerAPI.deleteBoxer(indexToDelete)
        if (boxerToDelete != null) {
            println("Delete Successful! Deleted boxer: ${boxerToDelete.boxerName}")
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
        //pass the index of the note to boxerAPI for archiving and check for success.
        if (boxerAPI.archiveNote(indexToArchive)) {
            println("Archive Successful!")
        } else {
            println("Archive NOT Successful")
        }
    }
}

fun searchBoxers() {
    val searchName = readNextLine("Enter description to search: ")
    val searchResults = boxerAPI.searchByName(searchName).toString()
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
    println("Exiting...bye")
    exit(0)
}