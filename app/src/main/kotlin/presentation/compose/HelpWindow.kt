package presentation.compose

import Constants.APP_ICON_PATH
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState

@Composable
fun HelpWindow(viewModel: AppViewModel) {
    val helpWindowIsVisible = viewModel.helpWindowIsVisible.collectAsState()
    if (helpWindowIsVisible.value) {
        Window(
            onCloseRequest = { viewModel.onHelpWindowCloseClick() },
            state = WindowState(width = 500.dp, height = 700.dp),
            resizable = false,
            title = "HELP",
            icon = painterResource(APP_ICON_PATH),
        ) {
            val helpText = """
                В общем-то помощь
                
                
                Для того чтобы запустить выполнение - кликнуть кнопку F4
                Для того чтобы завершить выполнение - кликнуть кнопку F5
                
                Note: выполнение останавливается не мгновенное. Оно докачает перк, если уже начало, и только потом остановится.
                
                
                Levels to upgrade
                Количество циклов, кототрое программа будет пытаться качать паутину.
                В идеале она бы качала каждый левел как реальный левел, но ещё недобита логика
                1) прокачка престижа считается как уровень
                2) пропуск нотификации после уровня или открытия слота перка считается как уровень
                3) впринципе алгоритмы могут что-то не расспознать корректно или прокачать не то и оно попробует ещё раз прокачать уровень с того же состония, на котором сломалось в прошлый раз. Таким образом прокачка одного уровня может стать и как 2 и больше и вообще все может сломаться нахуй, что уж
                
               
                Виды запуска.               
                3 вида запуска сейчас есть: Test, Simple, Rarest
                
                Test - мгновенно после F4 делает скриншот того что видит и пытается проанализировать что сейчас нарисовано и сконвертить в инфу о кружках(Node). Для того чтобы проверить, правильно ли оно всё распознает - можно тыкать и сверять. Буду говорить спасибо (возможно)
                
                Simple - логика простая. Качает всё что может по кругам, начиная с внутреннего. Прокачка осуществялется по часовой стрелке и начинается с 12 часов. 
                Он просто вкачает всё что можно и ни о чем не думает. Ему хорошо.
                
                Rarest - Логика более хитрая. Анализирует весь граф и пытается найти там самый дорогой Node. Потом добирается до него как может (не осуждаем).
                Как только самый дорогой нод взят - оно будет искать новый самый дорогой.
                
                Потом я сделаю Помощь красивее, но это потом.
            """.trimIndent()

            Text(helpText)
        }
    }
}