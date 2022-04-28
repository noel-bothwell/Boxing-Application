package models

data class Boxer(var boxerName: String, var boxerWins: Int, var boxerClass: String, var isBoxerArchived :Boolean){
    override fun toString(): String {
        return "Boxer(boxerName='$boxerName', boxerWins=$boxerWins, noteCategory='$boxerClass', isNoteArchived=$isBoxerArchived)"
    }
}