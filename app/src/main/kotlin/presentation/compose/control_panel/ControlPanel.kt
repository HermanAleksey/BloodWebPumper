package presentation.compose.control_panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import helper.Command

@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    setSelectedMode: (Command.Mode) -> Unit,
    levelsToUpgrade: Int,
    setLevelsToUpgrade: (Int) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "BloodWeb upgrade",
                style = TextStyle(fontSize = 24.sp)
            )
        }
        ModesSelect(setSelectedMode)
        LevelSelect(levelsToUpgrade, setLevelsToUpgrade)
        HelperCard()
    }
}