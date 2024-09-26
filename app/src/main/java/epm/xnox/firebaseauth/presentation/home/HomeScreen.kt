package epm.xnox.firebaseauth.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import epm.xnox.firebaseauth.R

@Composable
fun HomeScreen(
    onNavigateToSignIn: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Header()
            }
            Body(onNavigateToSignIn, onNavigateToSignUp)
        }
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.image_header),
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.welcome_header_title),
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.welcome_header_description),
            fontWeight = FontWeight.Light,
            fontSize = 18.sp,
            style = TextStyle(
                letterSpacing = 0.5.em
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun Body(navigateToSignIn: () -> Unit, navigateToSignUp: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(id = R.string.welcome_body_title),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(id = R.string.welcome_body_description),
                color = Color.Black,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { navigateToSignIn() },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color.Black,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(text = stringResource(id = R.string.sign_in), color = Color.White)
                }
                Spacer(modifier = Modifier.width(15.dp))
                TextButton(
                    onClick = { navigateToSignUp() },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(text = stringResource(id = R.string.sign_up), color = Color.Black)
                }
            }
        }
    }
}