import Composables.HistoryElement
import Composables.Recomposition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.github.AstethixOS.KotlinShell.CommandOutput
import com.github.AstethixOS.KotlinShell.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun App(
    exit: () -> Unit,
    changeMaximizedState: () -> Unit
) {
    var test by remember { mutableStateOf("d") }
    var drawerState = rememberDrawerState(DrawerValue.Closed)

    var History by rememberSaveable {
        mutableStateOf(
            listOf<CommandOutput>()
        )
    }

    var Shell by rememberSaveable {
        mutableStateOf(
            Shell(
                exit = { exit.invoke() },
                onHistoryChange = {
                    History = listOf()
                    History = this
                }
            )
        )
    }

    var CurrentTypedCommand by rememberSaveable { mutableStateOf("") }
    var drawerStateSwitcherScope = rememberCoroutineScope()
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {

                }
            }
        ) {

            Scaffold(
                containerColor = Color(0, 0, 0, 150),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Quasar"
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    drawerStateSwitcherScope.launch {
                                        if (drawerState.isOpen) {
                                            drawerState.close()
                                        } else {
                                            drawerState.open()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxHeight()
                                    .clip(RoundedCornerShape(100))
                                    .clickable(
                                        role = Role.DropdownList
                                    ) {

                                    }
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "Guest",
                                    modifier = Modifier.absolutePadding(
                                        right = 2.dp
                                    )
                                )

                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    modifier = Modifier.absolutePadding(
                                        left = 2.dp
                                    )
                                )
                            }

                            IconButton(
                                onClick = {
                                    exit.invoke()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    changeMaximizedState.invoke()
                                }
                            )
                        }
                    )
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().absolutePadding(
                        top = it.calculateTopPadding(),
                        left = it.calculateLeftPadding(LayoutDirection.Ltr)
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(10.dp)
                    ) {
                        for (i in History) {
                            item {
                                HistoryElement(
                                    commandOutput = i
                                )
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(20)).fillMaxWidth()
                                    .background(Color(100, 100, 100, 100))
                                    .padding(10.dp)
                            ) {
                                Row {
                                    Text(
                                        text = "~ ⚡ ",
                                        color = Color.White,
                                        fontFamily = NotoEmoji
                                    )

                                    BasicTextField(
                                        value = CurrentTypedCommand,
                                        onValueChange = { value: String ->
                                            CurrentTypedCommand = value
                                        },
                                        modifier = Modifier.fillMaxHeight().fillMaxWidth().onKeyEvent { event ->
                                            if (event.key == Key.Enter && event.type == KeyEventType.KeyDown) {
                                                var command = CurrentTypedCommand
                                                CurrentTypedCommand = ""
                                                Shell.Execute(command)
                                            }
                                            true
                                        },
                                        singleLine = true,
                                        cursorBrush = SolidColor(Color.White),
                                        textStyle = TextStyle(
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}