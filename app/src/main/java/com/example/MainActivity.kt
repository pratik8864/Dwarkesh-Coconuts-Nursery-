package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.BookingEntity
import com.example.data.LocalBrandingConfig
import com.example.data.ProductEntity
import com.example.ui.AppLanguage
import com.example.ui.LocalAppLanguage
import com.example.ui.components.AppTab
import com.example.ui.components.DwarkeshBottomNavigation
import com.example.ui.components.DwarkeshTopBar
import com.example.ui.screens.BookingReceiptScreen
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyOrdersScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PlantDetailScreen
import com.example.ui.screens.PlantsCatalogueScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.theme.DwarkeshTheme
import com.example.viewmodel.MainViewModel

sealed class ScreenState {
    object Splash : ScreenState()
    object Main : ScreenState()
    data class PlantDetail(val plantId: Long) : ScreenState()
    data class Booking(val plant: ProductEntity?) : ScreenState()
    data class BookingReceipt(val booking: BookingEntity) : ScreenState()
    object Notifications : ScreenState()
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentLanguage by viewModel.currentLanguage.collectAsState()
            val brandingConfig by viewModel.brandingConfig.collectAsState()
            val notifications by viewModel.notifications.collectAsState()
            val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()

            var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Splash) }
            var currentTab by remember { mutableStateOf(AppTab.HOME) }

            CompositionLocalProvider(
                LocalAppLanguage provides currentLanguage,
                LocalBrandingConfig provides brandingConfig
            ) {
                DwarkeshTheme {
                    when (val state = screenState) {
                        is ScreenState.Splash -> {
                            SplashScreen(
                                onSplashFinished = {
                                    screenState = ScreenState.Main
                                }
                            )
                        }

                        is ScreenState.PlantDetail -> {
                            BackHandler { screenState = ScreenState.Main }
                            PlantDetailScreen(
                                plantId = state.plantId,
                                viewModel = viewModel,
                                onBackClick = { screenState = ScreenState.Main },
                                onBookClick = { plant ->
                                    screenState = ScreenState.Booking(plant)
                                },
                                onAdminClick = {
                                    currentTab = AppTab.ADMIN
                                    screenState = ScreenState.Main
                                }
                            )
                        }

                        is ScreenState.Booking -> {
                            BackHandler { screenState = ScreenState.Main }
                            BookingScreen(
                                initialPlant = state.plant,
                                viewModel = viewModel,
                                onBackClick = { screenState = ScreenState.Main },
                                onBookingSuccess = { booking ->
                                    screenState = ScreenState.BookingReceipt(booking)
                                }
                            )
                        }

                        is ScreenState.BookingReceipt -> {
                            BackHandler { screenState = ScreenState.Main }
                            BookingReceiptScreen(
                                booking = state.booking,
                                viewModel = viewModel,
                                onBackHome = { screenState = ScreenState.Main }
                            )
                        }

                        is ScreenState.Notifications -> {
                            BackHandler { screenState = ScreenState.Main }
                            NotificationsScreen(
                                viewModel = viewModel,
                                onBackClick = { screenState = ScreenState.Main }
                            )
                        }

                        is ScreenState.Main -> {
                            // Back handler for tabs: if not on Home, press back to go to Home
                            if (currentTab != AppTab.HOME) {
                                BackHandler { currentTab = AppTab.HOME }
                            }

                            Scaffold(
                                topBar = {
                                    // Hide TopBar in Admin panel to allow dedicated admin header
                                    if (currentTab != AppTab.ADMIN || !isAdminLoggedIn) {
                                        DwarkeshTopBar(
                                            currentLanguage = currentLanguage,
                                            onLanguageToggle = { viewModel.toggleLanguage() },
                                            notificationCount = notifications.count { !it.isRead },
                                            onNotificationClick = { screenState = ScreenState.Notifications }
                                        )
                                    }
                                },
                                bottomBar = {
                                    DwarkeshBottomNavigation(
                                        selectedTab = currentTab,
                                        onTabSelected = { currentTab = it }
                                    )
                                }
                            ) { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {
                                    AnimatedContent(
                                        targetState = currentTab,
                                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                                        label = "tab_transition"
                                    ) { tab ->
                                        when (tab) {
                                            AppTab.HOME -> HomeScreen(
                                                viewModel = viewModel,
                                                onNavigateToPlants = { currentTab = AppTab.PLANTS },
                                                onNavigateToPlantDetail = { id ->
                                                    screenState = ScreenState.PlantDetail(id)
                                                },
                                                onNavigateToBooking = { plant ->
                                                    screenState = ScreenState.Booking(plant)
                                                },
                                                onNavigateToGallery = { currentTab = AppTab.GALLERY },
                                                onNavigateToAdmin = { currentTab = AppTab.ADMIN }
                                            )

                                            AppTab.PLANTS -> PlantsCatalogueScreen(
                                                viewModel = viewModel,
                                                onPlantClick = { id ->
                                                    screenState = ScreenState.PlantDetail(id)
                                                },
                                                onBookClick = { plant ->
                                                    screenState = ScreenState.Booking(plant)
                                                },
                                                onAdminClick = { currentTab = AppTab.ADMIN }
                                            )

                                            AppTab.BOOKINGS -> MyOrdersScreen(
                                                viewModel = viewModel,
                                                onViewReceipt = { booking ->
                                                    screenState = ScreenState.BookingReceipt(booking)
                                                },
                                                onAdminClick = { currentTab = AppTab.ADMIN }
                                            )

                                            AppTab.GALLERY -> GalleryScreen(
                                                viewModel = viewModel,
                                                onAdminClick = { currentTab = AppTab.ADMIN }
                                            )

                                            AppTab.ADMIN -> {
                                                if (isAdminLoggedIn) {
                                                    AdminDashboardScreen(
                                                        viewModel = viewModel,
                                                        onViewReceipt = { booking ->
                                                            screenState = ScreenState.BookingReceipt(booking)
                                                        },
                                                        onLogout = { /* Handled in VM */ }
                                                    )
                                                } else {
                                                    AdminLoginScreen(
                                                        viewModel = viewModel,
                                                        onLoginSuccess = { /* Refreshes automatically */ }
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
            }
        }
    }
}

