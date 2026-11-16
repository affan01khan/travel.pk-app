package pk.travel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF12345A)
private val Blue = Color(0xFF1673C8)
private val Pale = Color(0xFFF3F7FB)

private data class TravelSection(val title: String, val subtitle: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
private val sections = listOf(
    TravelSection("Flights", "Search and compare flight options", Icons.Default.Flight),
    TravelSection("My Trips", "View booking status and trip details", Icons.Default.Luggage),
    TravelSection("Plan a Stay", "Plan accommodation and request help", Icons.Default.Hotel),
    TravelSection("Travel Packages", "Explore sample itineraries and request a quote", Icons.Default.Explore),
    TravelSection("Destinations", "Guides for places around the world", Icons.Default.Place),
    TravelSection("Visa Assistance", "Understand documents and request assistance", Icons.Default.Description),
    TravelSection("Umrah & Hajj", "Pilgrimage planning and support", Icons.Default.Mosque),
    TravelSection("Travel Insurance", "Understand cover and request assistance", Icons.Default.Shield),
    TravelSection("Price Alerts", "Plan a route to monitor for fare changes", Icons.Default.Notifications),
    TravelSection("Help & Support", "Bookings, payments, changes and cancellations", Icons.Default.SupportAgent),
    TravelSection("About Travel.pk", "How the platform works", Icons.Default.Info),
    TravelSection("Contact", "Contact the Lahore support team", Icons.Default.Call)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TravelPkApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TravelPkApp() {
    var selected by remember { mutableStateOf("Home") }
    var from by remember { mutableStateOf("Lahore (LHE)") }
    var to by remember { mutableStateOf("") }
    var departure by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var passengers by remember { mutableStateOf("1 Adult") }
    var tripType by remember { mutableStateOf("Return") }
    var searchMessage by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Blue, secondary = Navy, background = Pale, surface = Color.White)
    ) {
        Scaffold(
            containerColor = Pale,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(36.dp).background(Blue, RoundedCornerShape(11.dp)),
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Default.FlightTakeoff, null, tint = Color.White) }
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("Travel.pk", fontWeight = FontWeight.Bold, color = Navy, fontSize = 19.sp)
                                Text("Plan your journey", color = Color.Gray, fontSize = 10.sp)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    listOf(
                        Triple("Home", Icons.Default.Home, "Home"),
                        Triple("Flights", Icons.Default.Flight, "Flights"),
                        Triple("My Trips", Icons.Default.Luggage, "My Trips"),
                        Triple("Help", Icons.Default.Help, "Help & Support")
                    ).forEach { (label, icon, destination) ->
                        NavigationBarItem(
                            selected = selected == destination || (label == "Help" && selected == "Help & Support"),
                            onClick = { selected = destination },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label, fontSize = 10.sp) }
                        )
                    }
                }
            }
        ) { padding ->
            when (selected) {
                "Home" -> HomeScreen(Modifier.padding(padding), onSelect = { selected = it })
                "Flights" -> FlightScreen(
                    Modifier.padding(padding), from, { from = it }, to, { to = it },
                    departure, { departure = it }, returnDate, { returnDate = it },
                    passengers, { passengers = it }, tripType, { tripType = it },
                    searchMessage, { searchMessage = it }
                )
                "My Trips" -> InfoScreen(Modifier.padding(padding), "My Trips",
                    "Your trip records will appear here after you sign in or provide a booking reference. A payment attempt is not a confirmed ticket. Only a provider-confirmed booking is treated as issued.",
                    listOf("Enter your booking reference", "Check whether a booking is processing, confirmed or needs review", "Contact support if a booking needs attention"))
                "Help & Support" -> InfoScreen(Modifier.padding(padding), "Help & Support",
                    "Travel.pk support can help with flight bookings, payments, refunds, changes, cancellations, check-in and travel documents.",
                    listOf("Phone: +92 42 3569 2952", "Email: info@travel.pk", "Support is available in Urdu and English during published staffed hours"))
                else -> SectionScreen(Modifier.padding(padding), selected)
            }
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier, onSelect: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(20.dp)) {
                    Text("Travel with clarity", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("Compare live flight options, understand fare conditions and keep track of your trip.", color = Color.White.copy(alpha = .9f))
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { onSelect("Flights") }, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Navy)) {
                        Icon(Icons.Default.Flight, null); Spacer(Modifier.width(8.dp)); Text("Search flights")
                    }
                }
            }
        }
        item { Text("Explore Travel.pk", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Navy) }
        items(sections) { section ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onSelect(section.title) },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(44.dp).background(Pale, RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
                        Icon(section.icon, null, tint = Blue)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(section.title, color = Navy, fontWeight = FontWeight.SemiBold)
                        Text(section.subtitle, color = Color.Gray, fontSize = 12.sp)
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
                }
            }
        }
        item {
            Text("Travel.pk is operated by TechAbout Private Limited. Flight prices and availability are returned by connected providers and may change before confirmation.", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlightScreen(
    modifier: Modifier, from: String, setFrom: (String) -> Unit, to: String, setTo: (String) -> Unit,
    departure: String, setDeparture: (String) -> Unit, returnDate: String, setReturnDate: (String) -> Unit,
    passengers: String, setPassengers: (String) -> Unit, tripType: String, setTripType: (String) -> Unit,
    message: String, setMessage: (String) -> Unit
) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Find your flight", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text("Compare the full journey—not only the headline fare.", color = Color.Gray)
        }
        item {
            Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = tripType == "Return", onClick = { setTripType("Return") }, label = { Text("Return") })
                        FilterChip(selected = tripType == "One way", onClick = { setTripType("One way") }, label = { Text("One way") })
                    }
                    OutlinedTextField(from, setFrom, label = { Text("From city or airport") }, leadingIcon = { Icon(Icons.Default.FlightTakeoff, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(to, setTo, label = { Text("To city or airport") }, leadingIcon = { Icon(Icons.Default.FlightLand, null) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(departure, setDeparture, label = { Text("Departure date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    if (tripType == "Return") OutlinedTextField(returnDate, setReturnDate, label = { Text("Return date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(passengers, setPassengers, label = { Text("Passengers (e.g. 1 Adult)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    Button(
                        onClick = {
                            setMessage(if (to.isBlank() || departure.isBlank()) "Enter destination and departure date to prepare a search. Live fares require the Travel.pk flight supplier connection." else "Search details saved in this form. Live fare results are not connected in this app build yet.")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Icon(Icons.Default.Search, null); Spacer(Modifier.width(8.dp)); Text("Search flights") }
                    if (message.isNotBlank()) Text(message, color = Navy, fontSize = 13.sp)
                }
            }
        }
        item {
            InfoCard("Before you book", "Check baggage, change and cancellation rules, total price and payment method. Fares can change or expire before the provider confirms an order.")
        }
    }
}

@Composable
private fun SectionScreen(modifier: Modifier, section: String) {
    val copy = when (section) {
        "Plan a Stay" -> "Plan accommodation by sharing your destination, dates, room setup and preferred hotel range. Live hotel inventory is not currently searchable online; stay planning is handled by request assistance."
        "Travel Packages" -> "Explore curated sample itineraries such as Hunza, Skardu and Azerbaijan. Packages can be customised. Request a dated, itemised quote that names hotels, transport, meals, taxes, exclusions and cancellation terms."
        "Destinations" -> "Explore destination guides including Hunza, Skardu, Lahore, Istanbul, Cappadocia and Dubai. Review seasons, arrival advice and practical planning notes before choosing travel dates."
        "Visa Assistance" -> "Review visa and document guidance for your destination. Requirements change, so verify the final rules with the relevant embassy, government authority and airline before travel. Request assistance for your application."
        "Umrah & Hajj" -> "Plan pilgrimage travel by checking dates, documents, accommodation, transport and ground arrangements. Contact the Travel.pk team for current package details and a written quotation."
        "Travel Insurance" -> "Understand travel insurance before departure. Review medical cover, trip interruption, baggage and exclusions in the policy wording. Confirm eligibility and coverage directly with the insurer."
        "Price Alerts" -> "A price alert can help you track a route over time instead of relying on an undated promotional fare. Alert delivery and live fare monitoring require a connected fare-data service."
        "About Travel.pk" -> "Travel.pk is operated by TechAbout Private Limited from Lahore. The platform compares current travel options and separates a live search result, a payment attempt and a confirmed booking. Only a provider-confirmed booking is treated as issued."
        "Contact" -> "Travel.pk customer support\nPhone: +92 42 3569 2952\nEmail: info@travel.pk\nAddress: 231-B Main Boulevard, Commercial Broadway, Sector B, Phase 8, DHA, Lahore 53000, Pakistan.\nPublished hours: Monday–Friday, 10 AM–7 PM PKT."
        else -> "This section is available inside the app shell. Detailed live content and service actions will be enabled when the corresponding Travel.pk data and service connections are integrated."
    }
    val bullets = when (section) {
        "Travel Packages" -> listOf("Hunza Valley: sample 5–8 day itinerary", "Skardu: sample 5–8 day itinerary", "Azerbaijan: request a tailored itinerary", "Ask for an itemised quote for your actual dates and group")
        "Destinations" -> listOf("Hunza — lakes, valleys and mountain scenery", "Skardu — gateway to the Karakoram and Deosai", "Lahore — heritage, food and history", "Istanbul, Cappadocia and Dubai")
        "Visa Assistance" -> listOf("Check passport validity", "Check visa and transit rules", "Prepare supporting documents", "Confirm current requirements with official authorities")
        "Umrah & Hajj" -> listOf("Travel dates and duration", "Visa and documentation", "Hotel and room arrangements", "Ground transport and support")
        "Travel Insurance" -> listOf("Medical and emergency cover", "Trip cancellation or interruption", "Baggage and document protection", "Policy limits, exclusions and claim process")
        "My Trips" -> listOf("Booking reference", "Passenger and itinerary details", "Payment and booking status", "Provider confirmation")
        else -> listOf("Review the service information", "Check current conditions and availability", "Request written confirmation before payment")
    }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(section, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy) }
        item { InfoCard(section, copy) }
        items(bullets) { bullet ->
            Row(verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.CheckCircle, null, tint = Blue, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                Text(bullet, color = Navy)
            }
        }
        item {
            InfoCard("Important", "This app screen currently provides native information and navigation. Live supplier inventory, user accounts, payment processing, booking changes and confirmations require Travel.pk's authorised backend/API connections.")
        }
    }
}

@Composable
private fun InfoScreen(modifier: Modifier, title: String, body: String, bullets: List<String>) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy) }
        item { InfoCard(title, body) }
        items(bullets) { Text("•  $it", color = Navy) }
    }
}

@Composable
private fun InfoCard(title: String, body: String) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, color = Navy, fontWeight = FontWeight.Bold)
            Text(body, color = Color.DarkGray, fontSize = 14.sp, lineHeight = 21.sp)
        }
    }
}
