package com.naufalmaulanaartocarpussavero607062300078.asesment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufalmaulanaartocarpussavero607062300078.asesment3.model.ReviewFilm
import com.naufalmaulanaartocarpussavero607062300078.asesment3.network.ApiStatus
import com.naufalmaulanaartocarpussavero607062300078.asesment3.network.FilmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<ReviewFilm>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveAllData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val result = FilmApi.service.getAllReviewFilm(userId)
                data.value = result
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }


    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = FilmApi.service.getReviewFilm(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, judul_film: String, rating: String, komentar: String ,bitmap: Bitmap) {
        Log.d("DEBUG", "saveData: UserId= $userId, judul_film= $judul_film, rating= $rating, komentar= $komentar")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = FilmApi.service.postReviewFilm(
                    userId,
                    judul_film.toRequestBody("text/plain".toMediaTypeOrNull()),
                    rating.toRequestBody("text/plain".toMediaTypeOrNull()),
                    komentar.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultiPartBody()
                )
                Log.d("tambah", "$result")
                if (result.status == "success")
                    retrieveData(userId)

                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"

            }
        }

    }

    fun updateData(userId: String, judul_film: String, rating: String, komentar: String, bitmap: Bitmap?, id: String) {
        Log.d("DEBUG", "saveData: UserId= $userId, judul_film= $judul_film, rating= $rating, komentar= $komentar")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = bitmap?.let {
                    FilmApi.service.updateReviewFilm(
                        userId,
                        judul_film.toRequestBody("text/plain".toMediaTypeOrNull()),
                        rating.toRequestBody("text/plain".toMediaTypeOrNull()),
                        komentar.toRequestBody("text/plain".toMediaTypeOrNull()),
                        it.toMultiPartBody(),
                        id
                    )
                }
                Log.d("edit", "$result")
                if (result != null) {
                    if (result.status == "success")
                        retrieveData(userId)
                    else
                        throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"

            }
        }

    }

    fun deleteData(userId: String, filmId: String) {
        Log.d("Delete", "delete Data: UserId= $userId, filmId= $filmId")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = FilmApi.service.deleteFilm(
                    userId = userId,
                    id = filmId
                )

                if (result.status == "success") {
                    retrieveData(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "Error delete: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }



    private fun Bitmap.toMultiPartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "poster", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null }


}