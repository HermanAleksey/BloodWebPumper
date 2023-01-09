package presentation.compose.main_screen.control_panel

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LevelSelect(levelsToUpgrade: Int, setLevelsToUpgrade: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Levels to upgrade: ")
            TextField(
                modifier = Modifier.width(60.dp),
                value = levelsToUpgrade.toString(),
                onValueChange = { text -> setLevelsToUpgrade(text.toIntOrNull() ?: 0) }
            )
        }
    }
}