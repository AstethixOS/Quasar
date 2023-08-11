package Composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.AstethixOS.KotlinShell.CommandOutput

@Composable
fun HistoryElement(
    commandOutput: CommandOutput
) {
    Column {
        Text(
            text = commandOutput.prompt,
            color = Color.White,
            modifier = Modifier.absolutePadding(
                top = 3.dp,
                bottom = 5.dp
            )
        )
        for (i in commandOutput.lines) {
            Text(
                text = i,
                color = Color.White,
                modifier = Modifier.absolutePadding(
                    left = 10.dp,
                    top = 3.dp,
                    bottom = 3.dp
                )
            )
        }
    }
}