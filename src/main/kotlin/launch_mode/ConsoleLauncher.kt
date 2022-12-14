package launch_mode

import CommandInterpreter
import Constants.CLOSE_PROGRAM_KEY
import Constants.EXECUTOR_KEY
import Constants.STOP_KEY
import execution_mode.ExecutionMode
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener

class ConsoleLauncher : AppLauncher {
    /**
     * F4 - is a trigger for starting operations.
     * after input of F4 program process inputted values
     * and run in configured mode
     *
     * Mode contains arguments: running mode, amount of levels to upgrade
     * in form of:
     * mXlYYF4
     * where X - is INT execution mode
     * Y - is INT number of levels to upgrade
     *
     * F5 press stops the script
     * Double click on F5 to stop the programm
     * */
    override fun run() {
        var continueRunning = true

        try {
            GlobalScreen.registerNativeHook()
            val commandInterpreter = CommandInterpreter()
            val stringBuffer = StringBuffer()
            var executor: ExecutionMode? = null

            GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
                override fun nativeKeyTyped(nativeEvent: NativeKeyEvent?) {}
                override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
                    val keyText: String = NativeKeyEvent.getKeyText(nativeEvent.keyCode)
                    stringBuffer.append(keyText)
                    if (keyText == EXECUTOR_KEY) {
                        val command = commandInterpreter.processStringCommand(stringBuffer.toString())
                        stringBuffer.delete(0, stringBuffer.length - 1)
                        command?.let {
                            executor = ExecutionMode.fromCommand(command = it)
                            executor?.run()
                        }
                    }
                    if (keyText == STOP_KEY) {
                        executor?.stop()
                        if (stringBuffer.length > 4) {
                            val lastFourSymbols = stringBuffer.substring(stringBuffer.length - 4, stringBuffer.length)
                            if (lastFourSymbols == CLOSE_PROGRAM_KEY) {
                                continueRunning = false
                            }
                        }
                    }
                }

                override fun nativeKeyPressed(nativeEvent: NativeKeyEvent?) {}
            })
        } catch (e: NativeHookException) {
            e.printStackTrace()
        }

        while (continueRunning) {
        }
    }
}