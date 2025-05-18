package ec.edu.uece.pa.grupo1torneovideojuegos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ec.edu.uece.pa.grupo1torneovideojuegos.ui.theme.Grupo1TorneoVideojuegosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Grupo1TorneoVideojuegosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EventCard(
                        title = "Gran Torneo de Videojuegos 2025",
                        organizer = "Organizado por Grupo 1",
                        date = "Sábado, 21 de Junio",
                        time = "De 10:00 AM a 18:00 PM",
                        location = "Universidad UCE, Facultad de Ingeniería\nAso Ingeniería",
                        contactEmail = "fing.direccion.computacion@uce.edu.ec",
                        contactPhone = "+593 999999999",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(
    title: String,
    organizer: String,
    date: String,
    time: String,
    location: String,
    contactEmail: String,
    contactPhone: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Imagen promocional
        Image(
            painter = painterResource(id = R.drawable.torneo_gaming),
            contentDescription = "Imagen promocional del torneo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        // Título del evento
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Organizador
        Text(
            text = organizer,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color(0xFF3700B3)
        )

        // Fecha y Hora (click para calendario)
        EventDetailCard(
            iconRes = R.drawable.ic_calendar,
            title = "Fecha y Hora",
            description = "$date\n$time",
            onClick = {
                val intent = Intent(Intent.ACTION_INSERT)
                intent.data = Uri.parse("content://com.android.calendar/events")
                intent.putExtra("title", title)
                intent.putExtra("description", "Torneo de Videojuegos organizado por $organizer")
                intent.putExtra("beginTime", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)
                intent.putExtra("endTime", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 + 1000 * 60 * 60 * 8)
                context.startActivity(intent)
            }
        )

        // Ubicación (click para Google Maps)
        EventDetailCard(
            iconRes = R.drawable.ic_location,
            title = "Lugar",
            description = location,
            onClick = {
                val gmmIntentUri = Uri.parse("https://maps.app.goo.gl/jue3b81z2wEuxdKZA")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Encabezado de Contacto
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_contact),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "Contacto",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF6200EE)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón de Email (funcionalidad específica)
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$contactEmail")
                            putExtra(Intent.EXTRA_SUBJECT, "Consulta sobre torneo de videojuegos")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6200EE))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = "Email",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar email")
                }

                // Información del email
                Text(
                    text = contactEmail,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Botón de Teléfono (funcionalidad específica)
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$contactPhone")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF03DAC6))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_phone),
                        contentDescription = "Teléfono",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Llamar ahora")
                }

                // Información del teléfono
                Text(
                    text = contactPhone,
                    fontSize = 14.sp,
                    color = Color.Gray)

            }
        }

        // Llamado a la acción
        Text(
            text = "¡Inscríbete ya! Cupos limitados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF03DAC6),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EventDetailCard(
    iconRes: Int,
    title: String,
    description: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp)
    )
     {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF6200EE))

                Text(
                    text = description,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardPreview() {
    Grupo1TorneoVideojuegosTheme {
        EventCard(
            title = "Gran Torneo de Videojuegos",
            organizer = "Organizado por Grupo 1",
            date = "Sábado, 25 de Noviembre",
            time = "De 10:00 AM a 18:00 PM",
            location = "Universidad UCE, Facultad de Ingeniería\nAso Ingeniería",
            contactEmail = "fing.direccion.computacion@uce.edu.ec",
            contactPhone = "+593 999999999"
        )
    }
}