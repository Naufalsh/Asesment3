package com.naufalmaulanaartocarpussavero607062300078.asesment3.ui.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.naufalmaulanaartocarpussavero607062300078.asesment3.R
import com.naufalmaulanaartocarpussavero607062300078.asesment3.ui.theme.Asesment3Theme

@Composable
fun FilmDialog(
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, String) -> Unit
) {
    var judul_film by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var komentar by remember { mutableStateOf("") }
    val isFormValid = judul_film.isNotBlank() && rating.isNotBlank() && komentar.isNotBlank()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card (
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                )
                OutlinedTextField(
                    value = judul_film,
                    onValueChange = { judul_film = it },
                    label = { Text(text = stringResource(id = R.string.judul_film)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { Text(text = stringResource(id = R.string.rating_film)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, // hanya angka
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = komentar,
                    onValueChange = { komentar = it },
                    label = { Text(text = stringResource(id = R.string.komentar_film)) },
                    maxLines = 5, // lebih dari 1 baris
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .heightIn(min = 100.dp) // memberi ruang komentar lebih luas
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.batal))
                    }
                    OutlinedButton(
                        onClick = { onConfirmation(judul_film, rating, komentar) },
                        enabled = isFormValid,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(R.string.simpan))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddDialogPreview() {
    Asesment3Theme {
        FilmDialog(
            bitmap = null,
            onDismissRequest = {},
            onConfirmation = { _, _, _ -> }
        )
    }
}