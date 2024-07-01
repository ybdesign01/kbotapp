package com.example.kbotapp.android

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val gotham = FontFamily(
    Font(R.font.light,FontWeight.Light),
    Font(R.font.medium, FontWeight.Medium),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
            WelcomeWidget()
            }

        }
    }
}

@Composable
fun LogoT() {
    Image(painterResource(R.drawable.kbot), "Logo", modifier = Modifier.requiredSize(100.dp))
}

@Preview("Welcome Activity")
@Composable
fun WelcomeWidget() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        LogoT()
        Text(
            text = "Bienvenue sur KBotApp",
            fontFamily = gotham,
            fontWeight = FontWeight.Medium,
            fontSize = 25.sp
        )
        Text(
            text = "Un bot simple à multi-usage.",
            fontFamily = gotham,
            fontWeight = FontWeight.Light,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.padding(0.dp, 100.dp, 0.dp, 0.dp))
        Text(
            text = " Veuillez entrer votre nom pour commencer:",
            fontFamily = gotham,
            fontWeight = FontWeight.Light,
            fontSize = 15.sp
        )
        TextField(
            modifier = Modifier
                .wrapContentHeight()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            singleLine = false,
            value = text,
            shape = RoundedCornerShape(15.dp),
            onValueChange = { text = it },
            placeholder = {
                Text(
                    text = "Entrez votre nom",
                    fontFamily = gotham,
                    fontWeight = FontWeight.Light,
                    fontSize = 15.sp
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
                .padding(20.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3862F5)),
            onClick = {
                if(text != ""){
                    var view = Intent(context,ChatActivity::class.java)
                    view.putExtra("nom", text)
                    context.startActivity(view)

                }else{
                    Toast.makeText(context,"Veuillez entrer un nom valide", Toast.LENGTH_SHORT).show()
                }

            }) {

               Text(text = "Continuer",
                       fontFamily = gotham,
                   color = Color.White
               )
        }

    }

}


