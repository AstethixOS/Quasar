package Composables

import androidx.compose.runtime.Composable

@Composable
fun Recomposition(
    value: Any,
    content: @Composable () -> Unit
) {
    println("recompose")
    content.invoke()
}