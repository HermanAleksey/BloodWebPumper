package presentation.compose.control_panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LevelSelect(levelsToUpgrade: Int, setLevelsToUpgrade: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text("Levels to upgrade:")
            TextField(
                value = levelsToUpgrade.toString(),
                onValueChange = { text -> setLevelsToUpgrade(text.toIntOrNull() ?: 0) }
            )
        }
    }
}