package com.example.proyectomovie_api.ui.view

import android.graphics.Region
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomovie_api.data.Repository
import com.example.proyectomovie_api.data.account.AccountDetailsResponse
import com.example.proyectomovie_api.data.inicioSesion.BodyLogin
import com.example.proyectomovie_api.data.inicioSesion.BodySessionID
import com.example.proyectomovie_api.data.inicioSesion.CreateGuestSessionResponse
import com.example.proyectomovie_api.data.inicioSesion.CreateSessionResponse
import com.example.proyectomovie_api.data.inicioSesion.RequestTokenResponse
import com.example.proyectomovie_api.data.movie.Movie
import com.example.proyectomovie_api.data.movie.MovieResponse
import com.example.proyectomovie_api.data.tv.TVShow
import kotlinx.coroutines.launch

class MyViewModel: ViewModel() {


    private val repositorio = Repository()
    private val listaMoviesPopuales = MutableLiveData<List<Movie>>()
    private val listaTVShowsPopulares = MutableLiveData<List<TVShow>>()
    private val listaMovieRated = MutableLiveData<List<Movie>>()
    private val listaTVShowRated = MutableLiveData<List<TVShow>>()
    private val listaPeliculasPopularesLiveData1 = MutableLiveData<MovieResponse>()

    private val requestToken = MutableLiveData<String>()
    private val sessionID = MutableLiveData<String>()

    //Obtener las peliculas más populares

    fun getPopularMovies(apiKey: String) : MutableLiveData<List<Movie>> {
        viewModelScope.launch {
            val respuesta = repositorio.getPopularMovies(apiKey)
            if (respuesta.code() == 200) {
                var listaPeliculasPopulares = respuesta.body()
                listaPeliculasPopulares?.let {
                    listaMoviesPopuales.postValue(it.results)
                }
            }
        }
        return listaMoviesPopuales

      fun getAuthToken() : MutableLiveData<String>{

        viewModelScope.launch {
            val response = repositorio.getAuthToken()

            if (response.code() == 200){
                response.body()?.request_token?.let {
                    requestToken.postValue(it)
                }
            }
        }
        return requestToken
    }

    fun createSessionLogin(body:BodyLogin) : MutableLiveData<RequestTokenResponse>{
        val liveData = MutableLiveData<RequestTokenResponse>()

        viewModelScope.launch {
            val response = repositorio.createSessionLogin(body)

            if (response.body()?.success == true){
                response.body()?.let {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    fun getSessionID() = sessionID

    fun setSessionID(id:String){
        sessionID.value = id
    }

    fun createGuestSession() : MutableLiveData<CreateGuestSessionResponse>{
        val liveData = MutableLiveData<CreateGuestSessionResponse>()

        viewModelScope.launch {
            val response = repositorio.createGuestSession ()

            if (response.code() == 200){
                response.body()?.let {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    fun createSession(bodySessionID: BodySessionID): MutableLiveData<CreateSessionResponse>{
        val liveData = MutableLiveData<CreateSessionResponse>()

        viewModelScope.launch {
            val response = repositorio.createSession(bodySessionID)

            if (response.code() == 200){
                response.body()?.let {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    fun getAccountDetails(sessionID: String): MutableLiveData<AccountDetailsResponse>{
        val liveData = MutableLiveData<AccountDetailsResponse>()

        viewModelScope.launch {
            val response = repositorio.getAccountDetails(sessionID)

            if (response.code() == 200){
                response.body()?.let {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    fun getPopularTVShow(apiKey: String) : MutableLiveData<List<TVShow>> {
        viewModelScope.launch {
            val respuesta = repositorio.getPopularTVShows(apiKey)
            if (respuesta.code() == 200) {
                var listaSeriesPopulares = respuesta.body()
                listaSeriesPopulares?.let {
                    listaTVShowsPopulares.postValue(it.results)
                }
            }
        }
        return listaTVShowsPopulares
    }

    fun topRatedMovies(apiKey: String) : MutableLiveData<List<Movie>> {
        viewModelScope.launch {
            val respuesta = repositorio.topRatedMovies(apiKey)
            if (respuesta.code() == 200) {
                var listaPeliculasRated = respuesta.body()
                listaPeliculasRated?.let {
                    listaMovieRated.postValue(it.results)
                }
            }
        }
        return listaMovieRated
    }

    fun topRatedTVShow(apiKey: String) : MutableLiveData<List<TVShow>> {
        viewModelScope.launch {
            val respuesta = repositorio.topRatedTVShows(apiKey)
            if (respuesta.code() == 200) {
                var listaSeriesRated = respuesta.body()
                listaSeriesRated?.let {
                    listaTVShowRated.postValue(it.results)
                }
            }
        }
        return listaTVShowRated
    }
}
