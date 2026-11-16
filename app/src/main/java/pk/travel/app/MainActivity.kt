package pk.travel.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF102A43)
private val Blue = Color(0xFF1769AA)
private val Pale = Color(0xFFF2F7FB)
private val Ink = Color(0xFF243B53)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TravelPkApp() }
    }
}

private enum class Tab(val label: String) {
    Home("Home"), Explore("Explore"), Packages("Packages"), Trips("My Trips"), Help("Help")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TravelPkApp() {
    var selected by remember { mutableStateOf(Tab.Home) }
    val context = LocalContext.current
    fun openSite(path: String = "") {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://travel.pk/" + path.trimStart('/'))))
    }
    MaterialTheme(colorScheme = lightColorScheme(primary = Blue, secondary = Navy, background = Pale)) {
        Scaffold(
            containerColor = Pale,
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FlightTakeoff, null, tint = Blue)
                        Spacer(Modifier.width(8.dp))
                        Text("Travel.pk", fontWeight = FontWeight.ExtraBold, color = Navy)
                    } },
                    actions = { IconButton(onClick = { openSite() }) { Icon(Icons.Default.OpenInBrowser, "Open website") } },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    Tab.entries.forEach { tab ->
                        val icon = when (tab) {
                            Tab.Home -> Icons.Default.Home
                            Tab.Explore -> Icons.Default.Explore
                            Tab.Packages -> Icons.Default.Luggage
                            Tab.Trips -> Icons.Default.ConfirmationNumber
                            Tab.Help -> Icons.Default.SupportAgent
                        }
                        NavigationBarItem(selected == tab, { selected = tab }, icon = { Icon(icon, tab.label) }, label = { Text(tab.label, fontSize = 10.sp) })
                    }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (selected) {
                    Tab.Home -> HomeScreen(::openSite)
                    Tab.Explore -> ExploreScreen(::openSite)
                    Tab.Packages -> PackagesScreen(::openSite)
                    Tab.Trips -> TripsScreen(::openSite)
                    Tab.Help -> HelpScreen(::openSite)
                }
            }
        }
    }
}

@Composable
private fun Page(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp), content = content)
}

@Composable
private fun Heading(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(title, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
        Text(subtitle, color = Ink, fontSize = 14.sp)
    }
}

@Composable
private fun Section(title: String) {
    Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Navy)
}

@Composable
private fun Item(title: String, subtitle: String, detail: String, action: String, click: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Navy, fontSize = 17.sp)
            Text(subtitle, color = Blue, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Text(detail, color = Ink, fontSize = 13.sp)
            Text("$action  →", color = Blue, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { click() }.padding(top = 4.dp))
        }
    }
}

@Composable
private fun HomeScreen(open: (String) -> Unit) {
    var from by remember { mutableStateOf("Lahore (LHE)") }
    var to by remember { mutableStateOf("Karachi (KHI)") }
    var depart by remember { mutableStateOf("") }
    var passengers by remember { mutableStateOf("1 Adult") }
    Page {
        Column(Modifier.fillMaxWidth().background(Navy, RoundedCornerShape(22.dp)).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Your journey starts here", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text("Plan and book your next trip with Travel.pk.", color = Color.White)
            Text("Flights • Holidays • Travel assistance", color = Color(0xFFD9EAF7), fontSize = 12.sp)
        }
        Section("Search flights")
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                OutlinedTextField(from, { from = it }, label = { Text("From") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(to, { to = it }, label = { Text("To") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(depart, { depart = it }, label = { Text("Departure date") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(passengers, { passengers = it }, label = { Text("Passengers") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Button(onClick = { open("") }, modifier = Modifier.fillMaxWidth()) { Text("Continue to live flight search") }
                Text("Live fares and availability are checked on Travel.pk. Prices may change until confirmed.", color = Color.Gray, fontSize = 11.sp)
            }
        }
        Section("Popular destinations")
        listOf("Hunza" to "Pakistan • Lakes, glaciers and mountain scenery",
            "Skardu" to "Pakistan • Deosai and the Karakoram",
            "Lahore" to "Pakistan • Food, culture and history",
            "Istanbul" to "Türkiye • Historic sites and the Bosphorus",
            "Dubai" to "UAE • City attractions and desert experiences").forEach {
            Item(it.first, "Destination guide", it.second, "Explore") { open("") }
        }
        Section("Travel services")
        Item("Visa assistance", "Travel preparation", "Review destination requirements and verify current rules with official authorities.", "Visit Travel.pk") { open("") }
        Item("Umrah & Hajj", "Pilgrimage planning", "Explore pilgrimage travel information and assistance.", "Visit Travel.pk") { open("") }
        Item("Holiday packages", "Tours and getaways", "Browse available travel package information.", "View packages") { open("packages/") }
    }
}

@Composable
private fun ExploreScreen(open: (String) -> Unit) {
    Page {
        Heading("Explore destinations", "Travel ideas and planning information.")
        listOf("Hunza" to "Attabad Lake, Passu Cones and the Karakoram",
            "Skardu" to "Shangrila, Deosai and mountain landscapes",
            "Lahore" to "Badshahi Mosque, food street and Mughal history",
            "Istanbul" to "Hagia Sophia, Grand Bazaar and Bosphorus",
            "Cappadocia" to "Cave hotels, valleys and hot-air balloons",
            "Dubai" to "Burj Khalifa, desert safari and shopping").forEach {
            Item(it.first, "Destination guide", it.second, "Open guide") { open("") }
        }
    }
}

@Composable
private fun PackagesScreen(open: (String) -> Unit) {
    Page {
        Heading("Holiday & tour packages", "Explore itineraries and request a dated quote.")
        listOf("Hunza Valley" to "Attabad Lake, Passu Cones and historic forts",
            "Skardu" to "Shangrila Resort, lakes and mountain scenery",
            "Fairy Meadows" to "Nanga Parbat viewpoints and alpine meadows",
            "Northern Pakistan" to "Karakoram Highway, Hunza, Skardu and Deosai",
            "Dubai" to "Request a custom itinerary and quote",
            "Azerbaijan" to "Ask about travel dates and package details").forEach {
            Item("${it.first} Tour Package", "Customisable itinerary", it.second, "View packages") { open("packages/") }
        }
        Button(onClick = { open("packages/") }, modifier = Modifier.fillMaxWidth()) { Text("Browse Travel.pk packages") }
    }
}

@Composable
private fun TripsScreen(open: (String) -> Unit) {
    Page {
        Heading("My Trips", "Review your Travel.pk booking online.")
        Item("View your booking", "Booking status and details",
            "Open Travel.pk to sign in and check your booking reference, ticket and current status.",
            "Open Travel.pk") { open("") }
        Text("A payment attempt is not the same as an issued ticket. Confirm that your booking is ticketed before travel.", color = Ink)
    }
}

@Composable
private fun HelpScreen(open: (String) -> Unit) {
    val ctx = LocalContext.current
    Page {
        Heading("How can we help?", "Travel support and booking assistance.")
        Item("Phone support", "+92 42 3569 2952", "Use your phone dialler to contact the Travel.pk team.", "Call") {
            ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+924235692952")))
        }
        Item("Email support", "info@travel.pk", "Send your question and include a booking reference if relevant.", "Email") {
            ctx.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:info@travel.pk")))
        }
        Item("Contact & WhatsApp", "Official contact page", "Find the available support channels and contact form.", "Contact us") { open("contact/") }
        Item("Help centre", "Bookings, changes and cancellations", "Find help with booking and travel questions.", "Get help") { open("help/") }
        Item("About Travel.pk", "About the platform", "Learn more about the travel service.", "Read more") { open("about/") }
    }
}
