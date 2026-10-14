package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.BondingMode
import com.example.model.InterfaceType
import com.example.model.NetworkInterfaceInfo
import com.example.network.MultiNetworkBondingManager
import com.example.ui.MainViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("OmniBond", appName)
  }

  @Test
  fun `network models initialization`() {
    val wifi = NetworkInterfaceInfo(
      id = "wifi_test",
      type = InterfaceType.WIFI,
      name = "Wi-Fi Test",
      isConnected = true
    )
    val sim1 = NetworkInterfaceInfo(
      id = "sim1_test",
      type = InterfaceType.SIM_1,
      name = "SIM 1 Test",
      isConnected = true
    )
    assertNotNull(wifi)
    assertNotNull(sim1)
    assertEquals(BondingMode.SPEED_AGGREGATION, BondingMode.valueOf("SPEED_AGGREGATION"))
  }

  @Test
  fun `multi-network manager discovers 3 channels`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val manager = MultiNetworkBondingManager(app)
    val interfaces = manager.interfaces.value

    assertEquals(3, interfaces.size)
    assertTrue(interfaces.any { it.type == InterfaceType.WIFI })
    assertTrue(interfaces.any { it.type == InterfaceType.SIM_1 })
    assertTrue(interfaces.any { it.type == InterfaceType.SIM_2 })
    manager.onDestroy()
  }

  @Test
  fun `socket test on interface returns success and duration`() = runBlocking {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val manager = MultiNetworkBondingManager(app)
    val wifi = manager.interfaces.value.first { it.type == InterfaceType.WIFI }

    val (success, latency) = manager.testSocketOnInterface(wifi)
    assertTrue(success)
    assertTrue(latency >= 0)
    manager.onDestroy()
  }

  @Test
  fun `viewmodel toggles bonding mode`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = MainViewModel(app)

    assertEquals(BondingMode.SPEED_AGGREGATION, viewModel.bondingMode.value)
    viewModel.setBondingMode(BondingMode.REDUNDANT_RACE)
    assertEquals(BondingMode.REDUNDANT_RACE, viewModel.bondingMode.value)

    viewModel.toggleBonding(false)
    assertEquals(false, viewModel.isBondingEnabled.value)
  }
}
