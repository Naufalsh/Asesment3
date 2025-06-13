package com.naufalmaulanaartocarpussavero607062300078.asesment3.ui.screen

import android.content.ContentResolver
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.naufalmaulanaartocarpussavero607062300078.asesment3.R
import com.naufalmaulanaartocarpussavero607062300078.asesment3.ui.theme.Asesment3Theme

@Composable
fun EditFilmDialog(
    bitmap: Bitmap?,
    currentJudul: String,
    currentRating: String,
    currentKomentar: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, String, Bitmap?) -> Unit
) {
    var judul_film by remember { mutableStateOf(currentJudul) }
    var rating by remember { mutableStateOf(currentRating) }
    var komentar by remember { mutableStateOf(currentKomentar) }
    val isFormValid = judul_film.isNotBlank() && rating.isNotBlank() && komentar.isNotBlank()

    val context = LocalContext.current

    var displayBitmap: Bitmap? by remember { mutableStateOf(bitmap) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        val croppedBitmap = getCroppedImage(context.contentResolver, it)
        if (croppedBitmap != null) {
            displayBitmap = croppedBitmap
        }
    }

    Log.e("bitmapEdit", "$displayBitmap")

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Use displayBitmap for the Image composable
                displayBitmap?.let {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                    ) {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                        IconButton(
                            onClick = {
                                val options = CropImageContractOptions(
                                    null, CropImageOptions(
                                        imageSourceIncludeGallery = true,
                                        imageSourceIncludeCamera = true,
                                        fixAspectRatio = true
                                    )
                                )
                                launcher.launch(options)
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .background(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Gambar",
                                tint = Color.White
                            )
                        }
                    }
                }

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
                        capitalization = KeyboardCapitalization.None,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = komentar,
                    onValueChange = { komentar = it },
                    label = { Text(text = stringResource(id = R.string.komentar_film)) },
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.batal))
                    }
                    OutlinedButton(
                        onClick = {
                            onConfirmation(judul_film, rating, komentar, displayBitmap)
                        },
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

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
) : Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun FilmEditDialogPreview() {
    Asesment3Theme {
        EditFilmDialog(
            bitmap = null,
            currentJudul = "Contoh Judul",
            currentRating = "4.5",
            currentKomentar = "Film ini sangat bagus dan menghibur!",
            onDismissRequest = {},
            onConfirmation = { _, _, _, _ -> }
        )
    }
}