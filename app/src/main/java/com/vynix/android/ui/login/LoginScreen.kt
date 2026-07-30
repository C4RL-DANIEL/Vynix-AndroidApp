package com.vynix.android.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val viewModel: LoginViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.success) {
        if (state.success) {
            onLoginSuccess()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Vynix",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Animated error area
            val errorVisible by remember(state.error) {
                derivedStateOf { state.error != null }
            }
            AnimatedVisibility(
                visible = errorVisible,
                enter = fadeIn(animationSpec = tween(300)) +
                        expandVertically(expandFrom = Alignment.Top, animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) +
                        shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(300))
            ) {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Animated button content (loading vs. text)
            AnimatedContent(
                targetState = state.isLoading,
                transitionSpec = {
                    if (targetState) {
                        (fadeIn(animationSpec = tween(200)) with
                                fadeOut(animationSpec = tween(200)))
                    } else {
                        (fadeIn(animationSpec = tween(200)) with
                                fadeOut(animationSpec = tween(200)))
                    }
                },
                label = "loginButton"
            ) { loading ->
                Button(
                    onClick = viewModel::login,
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Log in")
                    }
                }
            }
        }
    }
}
