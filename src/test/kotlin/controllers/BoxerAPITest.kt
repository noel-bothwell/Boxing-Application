package controllers

import models.Boxer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BoxerAPITest {

    private var mike: Boxer? = null
    private var john: Boxer? = null
    private var tyson: Boxer? = null
    private var jake: Boxer? = null
    private var logan: Boxer? = null
    private var populatedBoxers: BoxerAPI? = BoxerAPI(XMLSerializer(File("boxers.xml")))
    private var emptyBoxers: BoxerAPI? = BoxerAPI(XMLSerializer(File("boxers.xml")))

    @BeforeEach
    fun setup(){
        mike = Boxer("Mike", 5, "heavyweigth", false)
        john = Boxer("John", 1, "middleweight", false)
        tyson = Boxer("Tyson", 4, "cruiserweight", true)
        jake = Boxer("Jake", 4, "featherweight", false)
        logan = Boxer("Logan", 3, "featherweight", true)

        //adding 5 Boxers to the boxer api
        populatedBoxers!!.add(mike!!)
        populatedBoxers!!.add(john!!)
        populatedBoxers!!.add(tyson!!)
        populatedBoxers!!.add(jake!!)
        populatedBoxers!!.add(logan!!)
    }

    @AfterEach
    fun tearDown(){
        mike = null
        john = null
        tyson = null
        jake = null
        logan = null
        populatedBoxers = null
        emptyBoxers = null
    }

    @Nested
    inner class AddBoxers {
        @Test
        fun `adding a Boxer to a populated list adds to ArrayList`() {
            val newBoxer = Boxer("james", 1, "cruiserweigth", false)
            assertEquals(5, populatedBoxers!!.numberOfBoxers())
            assertTrue(populatedBoxers!!.add(newBoxer))
            assertEquals(6, populatedBoxers!!.numberOfBoxers())
            assertEquals(newBoxer, populatedBoxers!!.findBoxer(populatedBoxers!!.numberOfBoxers() - 1))
        }

        @Test
        fun `adding a Boxer to an empty list adds to ArrayList`() {
            val newBoxer = Boxer("james", 1, "cruiserweigth", false)
            assertEquals(0, emptyBoxers!!.numberOfBoxers())
            assertTrue(emptyBoxers!!.add(newBoxer))
            assertEquals(1, emptyBoxers!!.numberOfBoxers())
            assertEquals(newBoxer, emptyBoxers!!.findBoxer(emptyBoxers!!.numberOfBoxers() - 1))
        }
    }

    @Nested
    inner class UpdateBoxers {
        @Test
        fun `updating a Boxer that does not exist returns false`(){
            assertFalse(populatedBoxers!!.updateBoxer(6, Boxer("bob", 2, "welterweight", false)))
            assertFalse(populatedBoxers!!.updateBoxer(-1, Boxer("bob", 2, "welterweight", false)))
            assertFalse(emptyBoxers!!.updateBoxer(0, Boxer("bob", 2, "welterweight", false)))
        }

        @Test
        fun `updating a Boxer that exists returns true and updates`() {
            //check boxer 5 exists and check the contents
            assertEquals(logan, populatedBoxers!!.findBoxer(4))
            assertEquals("Logan", populatedBoxers!!.findBoxer(4)!!.boxerName)
            assertEquals(3, populatedBoxers!!.findBoxer(4)!!.boxerWins)
            assertEquals("featherweight", populatedBoxers!!.findBoxer(4)!!.boxerClass)

            //update boxer 5 with new information and ensure contents updated successfully
            assertTrue(populatedBoxers!!.updateBoxer(4, Boxer("Logan Paul", 2, "cruiserweight", false)))
            assertEquals("Logan Paul", populatedBoxers!!.findBoxer(4)!!.boxerName)
            assertEquals(2, populatedBoxers!!.findBoxer(4)!!.boxerWins)
            assertEquals("cruiserweight", populatedBoxers!!.findBoxer(4)!!.boxerClass)
        }
    }

    @Nested
    inner class DeleteBoxers {

        @Test
        fun `deleting a Boxer that does not exist, returns null`() {
            assertNull(emptyBoxers!!.deleteBoxer(0))
            assertNull(populatedBoxers!!.deleteBoxer(-1))
            assertNull(populatedBoxers!!.deleteBoxer(5))
        }

        @Test
        fun `deleting a Boxer that exists delete and returns deleted object`() {
            assertEquals(5, populatedBoxers!!.numberOfBoxers())
            assertEquals(logan, populatedBoxers!!.deleteBoxer(4))
            assertEquals(4, populatedBoxers!!.numberOfBoxers())
            assertEquals(mike, populatedBoxers!!.deleteBoxer(0))
            assertEquals(3, populatedBoxers!!.numberOfBoxers())
        }

    }

    @Nested
    inner class ArchiveBoxers {
        @Test
        fun `archiving a boxer that does not exist returns false`(){
            assertFalse(populatedBoxers!!.archiveBoxer(6))
            assertFalse(populatedBoxers!!.archiveBoxer(-1))
            assertFalse(emptyBoxers!!.archiveBoxer(0))
        }

        @Test
        fun `archiving an already archived boxer returns false`(){
            assertTrue(populatedBoxers!!.findBoxer(2)!!.isBoxerArchived)
            assertFalse(populatedBoxers!!.archiveBoxer(2))
        }

        @Test
        fun `archiving an active boxer that exists returns true and archives`() {
            assertFalse(populatedBoxers!!.findBoxer(1)!!.isBoxerArchived)
            assertTrue(populatedBoxers!!.archiveBoxer(1))
            assertTrue(populatedBoxers!!.findBoxer(1)!!.isBoxerArchived)
        }
    }

    @Nested
    inner class ListBoxers {

        @Test
        fun `listAllBoxers returns No Boxers Stored message when ArrayList is empty`() {
            assertEquals(0, emptyBoxers!!.numberOfBoxers())
            assertTrue(emptyBoxers!!.listAllBoxers().lowercase().contains("no boxers"))
        }

        @Test
        fun `listAllBoxers returns Boxers when ArrayList has boxers stored`() {
            assertEquals(5, populatedBoxers!!.numberOfBoxers())
            val boxersString = populatedBoxers!!.listAllBoxers().lowercase()
            assertTrue(boxersString.contains("mike"))
            assertTrue(boxersString.contains("john"))
            assertTrue(boxersString.contains("tyson"))
            assertTrue(boxersString.contains("jake"))
            assertTrue(boxersString.contains("logan"))
        }

        @Test
        fun `listActiveBoxers returns no active boxers stored when ArrayList is empty`() {
            assertEquals(0, emptyBoxers!!.numberOfActiveBoxers())
            assertTrue(
                emptyBoxers!!.listActiveBoxers().lowercase().contains("no active boxers")
            )
        }

        @Test
        fun `listActiveBoxers returns active boxers when ArrayList has active boxers stored`() {
            assertEquals(3, populatedBoxers!!.numberOfActiveBoxers())
            val activeBoxersString = populatedBoxers!!.listActiveBoxers().lowercase()
            assertTrue(activeBoxersString.contains("mike"))
            assertFalse(activeBoxersString.contains("tyson"))
            assertTrue(activeBoxersString.contains("john"))
            assertTrue(activeBoxersString.contains("jake"))
            assertFalse(activeBoxersString.contains("logan"))
        }

        @Test
        fun `listArchivedBoxers returns no archived boxers when ArrayList is empty`() {
            assertEquals(0, emptyBoxers!!.numberOfArchivedBoxers())
            assertTrue(
                emptyBoxers!!.listArchivedBoxers().lowercase().contains("no archived boxers")
            )
        }

        @Test
        fun `listArchivedBoxers returns archived boxers when ArrayList has archived boxers stored`() {
            assertEquals(2, populatedBoxers!!.numberOfArchivedBoxers())
            val archivedBoxersString = populatedBoxers!!.listArchivedBoxers().lowercase(Locale.getDefault())
            assertFalse(archivedBoxersString.contains("mike"))
            assertTrue(archivedBoxersString.contains("tyson"))
            assertFalse(archivedBoxersString.contains("john"))
            assertFalse(archivedBoxersString.contains("jake"))
            assertTrue(archivedBoxersString.contains("logan"))
        }

        @Test
        fun `listBoxersBySelectedWins returns No Boxers when ArrayList is empty`() {
            assertEquals(0, emptyBoxers!!.numberOfBoxers())
            assertTrue(emptyBoxers!!.listBoxersBySelectedWins(1).lowercase().contains("no boxers")
            )
        }

        @Test
        fun `listBoxersBySelectedWins returns no Boxers when no Boxers with the specific amount of wins exist`() {
            assertEquals(5, populatedBoxers!!.numberOfBoxers())
            val wins2String = populatedBoxers!!.listBoxersBySelectedWins(2).lowercase()
            assertTrue(wins2String.contains("no boxers"))
            assertTrue(wins2String.contains("2"))
        }

        @Test
        fun `listBoxersBySelectedWins returns all Boxers that match that amount of wins when boxers with that many wins exist`() {
            assertEquals(5, populatedBoxers!!.numberOfBoxers())
            val wins1String = populatedBoxers!!.listBoxersBySelectedWins(1).lowercase()
            assertTrue(wins1String.contains("1 boxer"))
            assertTrue(wins1String.contains("wins 1"))
            assertTrue(wins1String.contains("john"))
            assertFalse(wins1String.contains("logan"))
            assertFalse(wins1String.contains("mike"))
            assertFalse(wins1String.contains("tyson"))
            assertFalse(wins1String.contains("jake"))


            val wins4String = populatedBoxers!!.listBoxersBySelectedWins(4).lowercase(Locale.getDefault())
            assertTrue(wins4String.contains("2 boxer"))
            assertTrue(wins4String.contains("wins 4"))
            assertFalse(wins4String.contains("logan"))
            assertTrue(wins4String.contains("jake"))
            assertTrue(wins4String.contains("tyson"))
            assertFalse(wins4String.contains("mike"))
            assertFalse(wins4String.contains("john"))
        }

    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty boxers.XML file.
            val storingBoxers = BoxerAPI(XMLSerializer(File("boxers.xml")))
            storingBoxers.store()

            //Loading the empty boxers.xml file into a new object
            val loadedBoxers = BoxerAPI(XMLSerializer(File("boxers.xml")))
            loadedBoxers.load()

            //Comparing the source of the boxers (storingBoxers) with the XML loaded boxers (loadedBoxers)
            assertEquals(0, storingBoxers.numberOfBoxers())
            assertEquals(0, loadedBoxers.numberOfBoxers())
            assertEquals(storingBoxers.numberOfBoxers(), loadedBoxers.numberOfBoxers())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 boxers to the boxers.XML file.
            val storingBoxers = BoxerAPI(XMLSerializer(File("boxers.xml")))
            storingBoxers.add(jake!!)
            storingBoxers.add(logan!!)
            storingBoxers.add(john!!)
            storingBoxers.store()

            //Loading boxers.xml into a different collection
            val loadedBoxers = BoxerAPI(XMLSerializer(File("boxers.xml")))
            loadedBoxers.load()

            //Comparing the source of the boxers (storingBoxers) with the XML loaded boxers (loadedBoxers)
            assertEquals(3, storingBoxers.numberOfBoxers())
            assertEquals(3, loadedBoxers.numberOfBoxers())
            assertEquals(storingBoxers.numberOfBoxers(), loadedBoxers.numberOfBoxers())
            assertEquals(storingBoxers.findBoxer(0), loadedBoxers.findBoxer(0))
            assertEquals(storingBoxers.findBoxer(1), loadedBoxers.findBoxer(1))
            assertEquals(storingBoxers.findBoxer(2), loadedBoxers.findBoxer(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty boxers.json file.
            val storingBoxers = BoxerAPI(JSONSerializer(File("boxers.json")))
            storingBoxers.store()

            //Loading the empty boxers.json file into a new object
            val loadedBoxers = BoxerAPI(JSONSerializer(File("boxers.json")))
            loadedBoxers.load()

            //Comparing the source of the boxers (storingBoxers) with the json loaded boxers (loadedBoxers)
            assertEquals(0, storingBoxers.numberOfBoxers())
            assertEquals(0, loadedBoxers.numberOfBoxers())
            assertEquals(storingBoxers.numberOfBoxers(), loadedBoxers.numberOfBoxers())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 boxers to the boxers.json file.
            val storingBoxers = BoxerAPI(JSONSerializer(File("boxers.json")))
            storingBoxers.add(jake!!)
            storingBoxers.add(logan!!)
            storingBoxers.add(john!!)
            storingBoxers.store()

            //Loading boxers.json into a different collection
            val loadedBoxers = BoxerAPI(JSONSerializer(File("boxers.json")))
            loadedBoxers.load()

            //Comparing the source of the boxers (storingBoxers) with the json loaded notes (loadedBoxers)
            assertEquals(3, storingBoxers.numberOfBoxers())
            assertEquals(3, loadedBoxers.numberOfBoxers())
            assertEquals(storingBoxers.numberOfBoxers(), loadedBoxers.numberOfBoxers())
            assertEquals(storingBoxers.findBoxer(0), loadedBoxers.findBoxer(0))
            assertEquals(storingBoxers.findBoxer(1), loadedBoxers.findBoxer(1))
            assertEquals(storingBoxers.findBoxer(2), loadedBoxers.findBoxer(2))
        }

    }
    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfBoxersCalculatedCorrectly() {
            assertEquals(5, populatedBoxers!!.numberOfBoxers())
            assertEquals(0, emptyBoxers!!.numberOfBoxers())
        }

        @Test
        fun numberOfArchivedBoxersCalculatedCorrectly() {
            assertEquals(2, populatedBoxers!!.numberOfArchivedBoxers())
            assertEquals(0, emptyBoxers!!.numberOfArchivedBoxers())
        }

        @Test
        fun numberOfActiveBoxersCalculatedCorrectly() {
            assertEquals(3, populatedBoxers!!.numberOfActiveBoxers())
            assertEquals(0, emptyBoxers!!.numberOfActiveBoxers())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedBoxers!!.numberOfBoxersByWins(1))
            assertEquals(0, populatedBoxers!!.numberOfBoxersByWins(2))
            assertEquals(1, populatedBoxers!!.numberOfBoxersByWins(3))
            assertEquals(2, populatedBoxers!!.numberOfBoxersByWins(4))
            assertEquals(1, populatedBoxers!!.numberOfBoxersByWins(5))
            assertEquals(0, emptyBoxers!!.numberOfBoxersByWins(1))
        }
    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search boxers by name returns no boxers when no boxers with a name exist`() {
            //Searching a populated collection for a name that doesn't exist.
            assertEquals(5, populatedBoxers!!.numberOfBoxers())
            val searchResults = populatedBoxers!!.searchByName("no results expected").toString()
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyBoxers!!.numberOfBoxers())
            assertTrue(emptyBoxers!!.searchByName("").isEmpty())
        }

        @Test
        fun `search boxers by name return boxer when boxer with name exist`() {
            assertEquals(5, populatedBoxers!!.numberOfBoxers())


            var searchResults = populatedBoxers!!.searchByName("tyson")

            assertTrue(searchResults.contains("Tyson"))
            assertFalse(searchResults.contains("Logan"))

            //Searching a populated collection for a partial name that exists (case matches)
            searchResults = populatedBoxers!!.searchByName("o").toString()
            assertTrue(searchResults.contains("Tyson"))
            assertTrue(searchResults.contains("Logan"))
            assertFalse(searchResults.contains("super heavyweight"))

            //Searching a populated collection for a partial name that exists (case doesn't match)
            searchResults = populatedBoxers!!.searchByName("O")
            assertTrue(searchResults.contains("Tyson"))
            assertTrue(searchResults.contains("Logan"))
            assertFalse(searchResults.contains("super heavyweight"))
        }

    }



}