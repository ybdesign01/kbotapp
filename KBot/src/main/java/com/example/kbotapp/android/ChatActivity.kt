package com.example.kbotapp.android

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kbotapp.Message
import com.example.kbotapp.botResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


val UserMsgShape = RoundedCornerShape(33.dp, 33.dp, 33.dp, 33.dp)
val BotMsgShape = RoundedCornerShape(33.dp, 33.dp, 33.dp, 33.dp)


class ChatActivity : ComponentActivity() {
    val msgView by viewModels<MessageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                MainWidget(msgView)
            }

        }


    }
}

fun BotResponse(msg: Message, msgView: MessageViewModel, context: Context) {
    val r = botResponse.getResponse(msg.text)
    msgView.addMessage(Message(r, false))

    when (r) {
        /*"Ouverture de google..." -> {
            val google = Intent(Intent.ACTION_VIEW)
            google.data = Uri.parse("https://google.com")
            context.startActivity(google)
        }*/
        "Recherche..." -> {
            Timer().schedule(500) {
                val search = Intent(Intent.ACTION_VIEW)
                val searchKeyword = msg.text.lowercase().substringAfter("rechercher")
                search.data = Uri.parse("https://google.com/search?&q=$searchKeyword")
                context.startActivity(search)
            }
        }
        "Ouverture..." -> {
            try {
                val pm: PackageManager = context.packageManager
                val packages = pm.getInstalledApplications(0)
                val appName = msg.text.lowercase().substringAfter("ouvrir").trim()
                var packName = "com.example.pack"
                for (pack in packages) {
                    if (pm.getApplicationLabel(pack).toString().lowercase().equals(appName)) {
                        packName = pack.packageName
                        Log.d("YCR", pm.getApplicationLabel(pack).toString().lowercase())
                    }
                }

                val i: Intent? = pm.getLaunchIntentForPackage(packName)
                if (i != null) {
                    context.startActivity(i)
                } else {
                    msgView.addMessage(
                        Message(
                            "Application inexistante. Veuillez réessayer",
                            false
                        )
                    )
                }

            } catch (_: PackageManager.NameNotFoundException) {

            }
        }
        "Appel..." -> {
            val callIntent: Intent =
                Uri.parse("tel:${msg.text.lowercase().substringAfter("appeler")}").let { number ->
                    Intent(Intent.ACTION_DIAL, number)
                }
            context.startActivity(callIntent)
        }
        "Envoi..." -> {
            context.sendMail(msg.text.lowercase().substringAfter("mail").trim())
        }
    }


}

fun Context.sendMail(to: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email" // or "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, "")
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // TODO: Handle case where no email app is available
    } catch (t: Throwable) {
        // TODO: Handle potential other type of exceptions
    }
}

@Composable
fun Logo() {
    Image(painterResource(R.drawable.kbot), "Logo", modifier = Modifier.requiredSize(70.dp))
}


@Composable
fun MainWidget(msgViewModel: MessageViewModel) {

    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column {
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Logo()
        }
        Row {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Color(0x80F1F1F1),
                        shape = RoundedCornerShape(43.dp, 43.dp, 0.dp, 0.dp)
                    )
            ) {
                ChatSection(msgViewModel)
                Card(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(43.dp, 43.dp, 0.dp, 0.dp),
                    backgroundColor = Color.White,
                    elevation = 5.dp

                ) {
                    Row(
                        modifier = Modifier.padding(20.dp, 10.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        TextField(
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f),
                            singleLine = false,
                            value = text,
                            shape = RoundedCornerShape(15.dp),
                            onValueChange = { text = it },
                            placeholder = {
                                Text(
                                    text = "Entrez votre message...",
                                    fontFamily = gotham,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 14.sp
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(0x50B7B7B7),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                        )
                        Button(
                            modifier = Modifier
                                .padding(20.dp, 0.dp, 0.dp, 0.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3862F5)),
                            onClick = {
                                val message = Message(
                                    text,
                                    true
                                )
                                msgViewModel.addMessage(message)
                                text = ""

                                BotResponse(message, msgViewModel, context)
                            }) {
                            Image(
                                painterResource(R.drawable.send),
                                "send",
                                modifier = Modifier.requiredSize(20.dp)
                            )
                        }
                    }

                }
            }
        }


    }
}


@Composable
fun MessageBox(
    message: Message
) {
    val context = LocalContext.current
    val intent = (context as ChatActivity).intent
    val name = intent.getStringExtra("nom")
    val sdf = SimpleDateFormat("h:mm a", Locale.FRENCH)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isOut) Alignment.End else Alignment.Start,
    ) {
        if (message.isOut) {
            if (name != null) {
                Text(
                    text = name, fontSize = 12.sp,
                    color = Color(0xFF8F8F8F),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 10.dp, bottom = 5.dp)
                )
            }
        }
        if (message.text != "") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!message.isOut) {
                    Image(
                        painter = painterResource(R.drawable.bot),
                        contentDescription = "botAvatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)

                    )
                }



                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = if (message.isOut) Color(0xFF3862F5) else Color(0xFF31C220),
                            shape = if (message.isOut) UserMsgShape else BotMsgShape
                        )
                        .padding(15.dp)
                        .widthIn(0.dp, 200.dp)

                ) {
                    Column {

                        Text(message.text, color = Color.White)
                    }

                }
            }

        }
        val time = message.time.split(":")
        Text(
            text = time[0] + ":" + time[1], fontSize = 12.sp,
            color = Color(0xFF8F8F8F),
            modifier = if (message.isOut) Modifier.padding(
                end = 10.dp,
                top = 5.dp
            ) else Modifier.padding(
                start = 10.dp,
                top = 5.dp
            )
        )

    }
}

@Composable
fun ChatSection(
    msgViewModel: MessageViewModel,
    modifier: Modifier = Modifier.padding(bottom = 100.dp)
) {
    LazyColumn(

        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(43.dp, 43.dp, 0.dp, 0.dp)
            ),
        reverseLayout = true
    ) {
        items(msgViewModel.messages) {
            MessageBox(it)
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}
