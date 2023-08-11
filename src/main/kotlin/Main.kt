import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(
            onCloseRequest = {exitApplication()},
            transparent = true,
            undecorated = true,
            icon = painterResource("icon.svg"),
            title = "Quasar"
        ) {
            App(
                exit = {
                    exitApplication()
                },
                changeMaximizedState = {
                    if (window.placement != WindowPlacement.Maximized) {
                        window.placement = WindowPlacement.Maximized
                    } else {
                        window.placement = WindowPlacement.Floating
                    }
                }
            )
        }
    }
}