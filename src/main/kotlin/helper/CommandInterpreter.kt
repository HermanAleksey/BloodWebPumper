package helper

class CommandInterpreter {

    fun processStringCommand(commandLine: String): Command? {
        println("inputted line: $commandLine")
        val command: String = if (commandLine.length > 7) {
            val commandStartIndex = if (commandLine[commandLine.length - 7] != MODE_KEY) {
                commandLine.length - 6
            } else commandLine.length - 7
            commandLine.substring(commandStartIndex, commandLine.length)
        } else commandLine

        val modeStr = command[1].toString()
        val levelStr = command.substring(3 until command.indexOf(END_OF_COMMAND_SYMBOL))
        println("command:$command; decoded = modeValue:$modeStr, levelValue:$levelStr")

        //if input is correct
        return if (modeStr.isNumeric() && levelStr.isNumeric()) {
            val mode = modeStr.toInt()
            val level = levelStr.toInt()
            Command(mode, level)
        } else null
    }

    private fun String.isNumeric(): Boolean {
        val regex = "-?\\d+(\\.\\d+)?".toRegex()
        return this.matches(regex)
    }

    companion object {
        const val END_OF_COMMAND_SYMBOL = 'F'
        const val MODE_KEY = 'M'
        const val LEVELS_KEY = 'L'
    }
}