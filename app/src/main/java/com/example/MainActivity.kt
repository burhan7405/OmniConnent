package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.SimCard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.screens.BondedDownloaderScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.SimManagerScreen
import com.example.ui.screens.SpeedAggregatorScreen
import com.example.ui.theme.BondedGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.TechCardBorder
import com.example.ui.theme.TechNavyDark
import com.example.ui.theme.TechSurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                OmniBondApp(viewModel = viewModel)
            }
        }
    }
}

enum class NavTab(val title: String, val icon: ImageVector, val tag: String) {
    DASHBOARD("Bonding", Icons.Default.Bolt, "tab_dashboard"),
    SPEED_TEST("Speed Test", Icons.Default.Speed, "tab_speed_test"),
    SIM_MANAGER("Dual SIM", Icons.Default.SimCard, "tab_sim_manager"),
    DOWNLOADER("Downloader", Icons.Default.CloudDownload, "tab_downloader")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OmniBondApp(viewModel: MainViewModel) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val interfaces by viewModel.interfaces.collectAsStateWithLifecycle()
    val isBondingEnabled by viewModel.isBondingEnabled.collectAsStateWithLifecycle()
    val bondingMode by viewModel.bondingMode.collectAsStateWithLifecycle()
    val aggregatedSpeed by viewModel.aggregatedSpeedMbps.collectAsStateWithLifecycle()
    val totalBytes by viewModel.totalBytesAggregated.collectAsStateWithLifecycle()
    val speedMetrics by viewModel.speedTestMetrics.collectAsStateWithLifecycle()
    val downloadState by viewModel.downloadState.collectAsStateWithLifecycle()
    val isMobileDataAlwaysActive by viewModel.isMobileDataAlwaysActive.collectAsStateWithLifecycle()
    val hasPhonePermission by viewModel.hasPhonePermission.collectAsStateWithLifecycle()
    val socketResults by viewModel.socketTestResults.collectAsStateWithLifecycle()
    val testingSocketId by viewModel.isTestingSocket.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updatePermissionStatus(isGranted)
    }

    // Handle back button when on sub-tabs
    BackHandler(enabled = selectedTabIndex != 0) {
        selectedTabIndex = 0
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = TechNavyDark,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isBondingEnabled) BondedGreen else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "OmniBond",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = if (isBondingEnabled) BondedGreen.copy(alpha = 0.15f) else Color.DarkGray,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isBondingEnabled) "WIFI + 2x SIM" else "OFFLINE",
                                color = if (isBondingEnabled) BondedGreen else Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                actions = {
                    Text(
                        text = "${"%.1f".format(aggregatedSpeed)} Mbps",
                        color = if (isBondingEnabled) BondedGreen else TextMuted,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TechSurfaceDark,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = TechSurfaceDark,
                contentColor = Color.White,
                tonalElevation = 0.dp
            ) {
                NavTab.entries.forEachIndexed { index, tab ->
                    val isSelected = selectedTabIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PrimaryNeon,
                            selectedTextColor = PrimaryNeon,
                            indicatorColor = PrimaryNeon.copy(alpha = 0.15f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        when (selectedTabIndex) {
            0 -> DashboardScreen(
                interfaces = interfaces,
                isBondingEnabled = isBondingEnabled,
                bondingMode = bondingMode,
                aggregatedSpeed = aggregatedSpeed,
                totalBytes = totalBytes,
                isMobileDataAlwaysActive = isMobileDataAlwaysActive,
                socketResults = socketResults,
                testingSocketId = testingSocketId,
                onToggleBonding = { viewModel.toggleBonding(it) },
                onSelectBondingMode = { viewModel.setBondingMode(it) },
                onTestSocket = { viewModel.testSocket(it) },
                onRefresh = { viewModel.refreshNetworks() },
                onOpenDeveloperSettings = { viewModel.openDeveloperSettings() },
                modifier = Modifier.padding(innerPadding)
            )
            1 -> SpeedAggregatorScreen(
                metrics = speedMetrics,
                onStartSpeedTest = { viewModel.startSpeedTest() },
                modifier = Modifier.padding(innerPadding)
            )
            2 -> SimManagerScreen(
                interfaces = interfaces,
                hasPhonePermission = hasPhonePermission,
                onRequestPhonePermission = {
                    permissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
                },
                onRefresh = { viewModel.refreshNetworks() },
                onTestSocket = { viewModel.testSocket(it) },
                testingSocketId = testingSocketId,
                socketResults = socketResults,
                modifier = Modifier.padding(innerPadding)
            )
            3 -> BondedDownloaderScreen(
                downloadState = downloadState,
                onStartDownload = { viewModel.startBondedDownload(it) },
                onCancelDownload = { viewModel.cancelDownload() },
                onOpenDeveloperSettings = { viewModel.openDeveloperSettings() },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
