package com.xs.firenotesapp.ui.screens.profilescreen.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.xs.firenotesapp.R
import com.xs.firenotesapp.ui.components.StyledTextField
import com.xs.firenotesapp.ui.screens.profilescreen.viewmodel.ProfileViewModel
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collectLatest {
            when(it){
                is UiEvent.Navigate -> {
                    navController.navigate(it.route)
                }
                UiEvent.PopBackStack -> {
                    navController.popBackStack()
                }
                is UiEvent.ShowSnackBar -> {

                }
            }
        }
    })

    Scaffold {
        Column(Modifier.padding(it)) {
            Column(
                Modifier
                    .padding(it)
                    .fillMaxSize(),
                Arrangement.SpaceEvenly,
                Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "profile image"
                )
                ElevatedButton(onClick = { getContent.launch("image/*") }) {
                    Text(text = "Change Profile Image")
                }
                Text(text = "Email: ")
                Text(text = "Account Type: ")
                StyledTextField(
                    onValueChange = {},
                    modifier = Modifier,
                    value = "Name",
                    placeholder = "Name",
                    textStyle = TextStyle.Default,
                    leadingIcon = Icons.Default.Person,
                    trailingIcon = null,
                    visibility = true,
                )
                StyledTextField(
                    onValueChange = {},
                    modifier = Modifier,
                    value = "Age",
                    placeholder = "Age",
                    textStyle = TextStyle.Default,
                    leadingIcon = Icons.Default.Person,
                    trailingIcon = null,
                    visibility = true,
                )

                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Change Data")
                }

                Text(text = "Change Password ")
                StyledTextField(
                    onValueChange = {},
                    modifier = Modifier,
                    value = "Password",
                    placeholder = "Password",
                    textStyle = TextStyle.Default,
                    leadingIcon = Icons.Default.Person,
                    trailingIcon = null,
                    visibility = true,
                )

                StyledTextField(
                    onValueChange = {},
                    modifier = Modifier,
                    value = "Confirm Password",
                    placeholder = "Confirm Password",
                    textStyle = TextStyle.Default,
                    leadingIcon = Icons.Default.Person,
                    trailingIcon = null,
                    visibility = true,
                )

                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Change Password")
                }
            }
        }
    }

}