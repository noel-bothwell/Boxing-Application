package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import utils.ClassValidation.classes
import utils.ClassValidation.isValidClass
import utils.Validation.validRange


class UtilitiesTest {
    @Nested
    inner class validRange {
        @Test
        fun validRangeWorksWithPositiveTestNumbers() {
            Assertions.assertTrue(validRange(1, 1, 1))
            Assertions.assertTrue(validRange(1, 1, 2))
            Assertions.assertTrue(validRange(1, 0, 1))
            Assertions.assertTrue(validRange(1, 0, 2))
            Assertions.assertTrue(validRange(-1, -2, -1))
        }

        @Test
        fun validRangeWorksWithNegativeTestNumbers() {
            Assertions.assertFalse(validRange(1, 0, 0))
            Assertions.assertFalse(validRange(1, 1, 0))
            Assertions.assertFalse(validRange(1, 2, 1))
            Assertions.assertFalse(validRange(-1, -1, -2))
        }
    }

    @Nested
    inner class isValidClass {
        @Test
        fun classesReturnsFullClassesSet() {
            Assertions.assertEquals(5, classes.size)
            Assertions.assertTrue(classes.contains("lightweight"))
            Assertions.assertTrue(classes.contains("welterweight"))
            Assertions.assertTrue(classes.contains("middleweight"))
            Assertions.assertTrue(classes.contains("cruiserweight"))
            Assertions.assertTrue(classes.contains("heavyweight"))
            Assertions.assertFalse(classes.contains(""))
        }

        @Test
        fun isValidCategoryTrueWhenCategoryExists() {
            Assertions.assertTrue(isValidClass("lightweight"))
            Assertions.assertTrue(isValidClass("Lightweight"))
            Assertions.assertTrue(isValidClass("LIGHTWEIGHT"))
            Assertions.assertTrue(isValidClass("welterweight"))
            Assertions.assertTrue(isValidClass("Welterweight"))
            Assertions.assertTrue(isValidClass("WELTERWEIGHT"))
            Assertions.assertTrue(isValidClass("middleweight"))
            Assertions.assertTrue(isValidClass("Middleweight"))
            Assertions.assertTrue(isValidClass("MIDDLEWEIGHT"))
            Assertions.assertTrue(isValidClass("cruiserweight"))
            Assertions.assertTrue(isValidClass("Cruiserweight"))
            Assertions.assertTrue(isValidClass("CRUISERWEIGHT"))
            Assertions.assertTrue(isValidClass("heavyweight"))
            Assertions.assertTrue(isValidClass("Heavyweight"))
            Assertions.assertTrue(isValidClass("HEAVYWEIGHT"))
        }

        @Test
        fun isValidClassFalseWhenClassDoesNotExist() {
            Assertions.assertFalse(isValidClass("li"))
            Assertions.assertFalse(isValidClass("crruiserweight"))
            Assertions.assertFalse(isValidClass("welt"))
            Assertions.assertFalse(isValidClass("mid"))
            Assertions.assertFalse(isValidClass("heavvey"))
        }

    }
}
