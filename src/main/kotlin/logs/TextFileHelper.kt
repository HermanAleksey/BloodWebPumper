package logs

import java.io.File

class TextFileHelper {

    private var file: File

    init {
        try {
            val currentUserName: String = System.getProperty("user.name")
            val absoluteFilePath =
                "C:\\Users\\$currentUserName\\Documents\\BloodWebPumperLog.txt"

            file = File(absoluteFilePath)
        } catch (e: java.lang.Exception) {
            file = File("BloodWebPumperLog.txt")
            e.printStackTrace()
        }
    }


    fun appendFileLine(text: String) {
        file.appendText(text + "\n")
    }

    fun clearFile() {
        file.writeText("")
    }
}