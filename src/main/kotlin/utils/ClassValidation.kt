package utils

object ClassValidation {
    // NOTE: JvmStatic annotation means that the Boxerclass variable is static i.e. we can reference it through the class

    @JvmStatic
    val classes = setOf("lightweight", "welterweight", "middleweight", "cruiserweight", "heavyweight")

    @JvmStatic
    fun isValidClass(classToCheck: String?): Boolean {
        for (Class in classes) {
            if (Class.equals(classToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}