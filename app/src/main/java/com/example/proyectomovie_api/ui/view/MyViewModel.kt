package com.example.proyectomovie_api.ui.view

import android.widget.Toast
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectomovie_api.data.Repository
import com.example.proyectomovie_api.data.favorite.addFavoriteBody
import com.example.proyectomovie_api.data.images.ImageResponse
import com.example.proyectomovie_api.data.account.AccountDetailsResponse
import com.example.proyectomovie_api.data.inicioSesion.BodyLogin
import com.example.proyectomovie_api.data.inicioSesion.CreateGuestSessionResponse
import com.example.proyectomovie_api.data.inicioSesion.CreateSessionResponse
import com.example.proyectomovie_api.data.inicioSesion.RequestTokenResponse
import com.example.proyectomovie_api.data.movie.Movie
import com.example.proyectomovie_api.data.movie.MovieResponse
import com.example.proyectomovie_api.data.movieProvider.MovieProviderResponse
import com.example.proyectomovie_api.data.movie_detalles.MovieDetallesResponse
import com.example.proyectomovie_api.data.serie_detalles.SerieDetallesResponse
import com.example.proyectomovie_api.data.people.People
import com.example.proyectomovie_api.data.tv.TVShow
import com.example.proyectomovie_api.data.tvSerieProvider.TVProviderResponse
import com.example.proyectomovie_api.data.watchlist.WatchListResponse
import com.example.proyectomovie_api.data.watchlist.addWatchListBody
import kotlinx.coroutines.launch

class MyViewModel: ViewModel() {

    private val repositorio = Repository()
    private val listaMoviesPopuales = MutableLiveData<List<Movie>>()
    private val listaTVShowsPopulares = MutableLiveData<List<TVShow>>()
    private val listaMovieRated = MutableLiveData<List<Movie>>()
    private val listaTVShowRated = MutableLiveData<List<TVShow>>()
    private val listaPeliculasPopularesLiveData1 = MutableLiveData<MovieResponse>()
    private val peliculaLivedata = MutableLiveData<MovieDetallesResponse>()
    private val serieLiveData = MutableLiveData<SerieDetallesResponse>()
    private val requestToken = MutableLiveData<String>()
    private val sessionID = MutableLiveData<String>()
    private val listaFavMovies = MutableLiveData<List<Movie>>()
    private val listaFavSeries = MutableLiveData<List<TVShow>>()
    private val listaWLMoviesFav = MutableLiveData<List<Movie>>()
    private val listaWLTVShowsFav = MutableLiveData<List<TVShow>>()

    private val listaMovies = MutableLiveData<List<Movie>>()
    private val actorInfo = MutableLiveData<List<People>>()
    private val listaMoviesBuscador = MutableLiveData<List<Movie>>()
    private val listaShowBuscador = MutableLiveData<List<TVShow>>()
    private val listaBuscadorLiveData = MutableLiveData<List<Movie>>()
    private val listaBuscadorSerieLiveData = MutableLiveData<List<TVShow>>()
    private val userCategory = MutableLiveData<String>()

    //Id del acountId de Salva -> 548
    // Esto al juntarse con el resto del código tiene que cambiarse para que esté bien
    private val accountID = MutableLiveData<Int>()


    //Obtener las peliculas más populares

    fun getPopularMovies(): MutableLiveData<List<Movie>> {
        viewModelScope.launch {
            val respuesta = repositorio.getPopularMovies()
            if (respuesta.code() == 200) {
                val listaPeliculasPopulares = respuesta.body()
                listaPeliculasPopulares?.let {
                    listaMoviesPopuales.postValue(it.results)
                }
            }
        }
        return listaMoviesPopuales
    }

    fun getAuthToken(): MutableLiveData<String> {

        viewModelScope.launch {
            val response = repositorio.getAuthToken()

            if (response.code() == 200) {
                response.body()?.request_token?.let {
                    requestToken.postValue(it)
                }
            }
        }
        return requestToken
    }

    fun createSessionLogin(body: BodyLogin): MutableLiveData<RequestTokenResponse> {
        val liveData = MutableLiveData<RequestTokenResponse>()

        viewModelScope.launch {
            val response = repositorio.createSessionLogin(body)

            if (response.body()?.success == true) {
                response.body()?.let {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    fun getSessionID() = sessionID

    fun setSessionID(id: String) {
        sessionID.value = id
    }

    fun createGuestSession(): MutableLiveData<CreateGuestSessionResponse> {
        val liveData = MutableLiveData<CreateGuestSessionResponse>()

        viewModelScope.launch {
            val response = repositorio.createGuestSession()

            if (response.code() == 200) {
                response.body()?.let {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    fun createSession(authToken: String): MutableLiveData<CreateSessionResponse> {

        val liveData = MutableLiveData<CreateSessionResponse>()

        viewModelScope.launch {
            val response = repositorio.createSession(authToken)

            if (response.code() == 200) {
                response.body()?.let {
                    liveData.postValue(it)
                    sessionID.postValue(it.session_id)
                }
            }
        }

        return liveData
    }

    fun deleteSession(sessionID: String): MutableLiveData<Boolean> {
        val done = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val response = repositorio.deleteSession(sessionID)

            if (response.code() == 200) {
                response.body()?.let {
                    done.postValue(it.success)
                }
            } else {
                done.value = false
            }
        }
        return done
    }

    //Da un objeto Response con los detalles de la cuenta
    fun getAccountDetails(sessionID: String): MutableLiveData<AccountDetailsResponse> {
        val liveData = MutableLiveData<AccountDetailsResponse>()

        viewModelScope.launch {
            val response = repositorio.getAccountDetails(sessionID)

            if (response.code() == 200) {
                response.body()?.let {
                    liveData.postValue(it)
                }
            }
        }
        return liveData
    }

    fun getPopularTVShow(): MutableLiveData<List<TVShow>> {
        viewModelScope.launch {
            val respuesta = repositorio.getPopularTVShows()
            if (respuesta.code() == 200) {

                val listaSeriesPopulares = respuesta.body()
                listaSeriesPopulares?.let {
                    listaTVShowsPopulares.postValue(it.results)
                }
            }
        }
        return listaTVShowsPopulares
    }


    fun topRatedMovies(): MutableLiveData<List<Movie>> {
        viewModelScope.launch {
            val respuesta = repositorio.topRatedMovies()
            if (respuesta.code() == 200) {
                val listaPeliculasRated = respuesta.body()
                listaPeliculasRated?.let {
                    listaMovieRated.postValue(it.results)
                }
            }
        }
            return listaMovieRated
        }

    fun topRatedTVShow(): MutableLiveData<List<TVShow>> {
        viewModelScope.launch {
            val respuesta = repositorio.topRatedTVShows()
            if (respuesta.code() == 200) {
                val listaSeriesRated = respuesta.body()
                listaSeriesRated?.let {
                    listaTVShowRated.postValue(it.results)
                }
            }
        }
            return listaTVShowRated
        }


    // Devuelve un objeto con los proveedores de la Película
    fun getMovieWatchProvider(movieId: Int): MutableLiveData<MovieProviderResponse> {
        val movieWatchProviderLiveData = MutableLiveData<MovieProviderResponse>()
        viewModelScope.launch {
            val respuesta = repositorio.getMovieWatchProvider(movieId)
            if (respuesta.code() == 200) {
                val movieWatchProvider = respuesta.body()
                movieWatchProvider.let {
                    movieWatchProviderLiveData.postValue(it)
                }
            }
        }
        return movieWatchProviderLiveData
    }

    // Devuelve un objeto con los proveedores de la Serie
    fun getSerieWatchProvider(serieId: Int): MutableLiveData<TVProviderResponse> {
        val serieWatchProviderLiveData = MutableLiveData<TVProviderResponse>()
        viewModelScope.launch {
            val respuesta = repositorio.getSerieWatchProvider(serieId)
            if (respuesta.code() == 200) {
                val serieWatchProvider = respuesta.body()
                serieWatchProvider.let {
                    serieWatchProviderLiveData.postValue(it)
                }
            }
        }
        return serieWatchProviderLiveData
    }

    // Devuelve una respuesta si el POST de añadir a WatchList es exitoso
    fun addToWatchList(accountId: Int, data: addWatchListBody): MutableLiveData<WatchListResponse> {
        val addWatchListLiveData = MutableLiveData<WatchListResponse>()
        viewModelScope.launch {
            val respuesta = repositorio.addToWatchList(accountId, data)
            if (respuesta.code() == 200 || respuesta.code() == 201) {
                val addWatchList = respuesta.body()
                addWatchList.let {
                    addWatchListLiveData.postValue(it)
                }
            }
        }
        return addWatchListLiveData
    }

    fun addToFavorite(
        context: Context,
        accountId: Int,
        data: addFavoriteBody
    ): MutableLiveData<WatchListResponse> {
        val addFavoriteLiveData = MutableLiveData<WatchListResponse>()
        viewModelScope.launch {
            val respuesta = repositorio.addToFavorite(accountId, data)
            if (respuesta.code() == 200 || respuesta.code() == 201) {
                val addFavorite = respuesta.body()
                addFavorite.let {
                    addFavoriteLiveData.postValue(it)
                }
            } else {
                Toast.makeText(context, respuesta.code().toString(), Toast.LENGTH_SHORT).show()
            }
        }
        return addFavoriteLiveData
    }

    fun getMovieImages(movieId: Int): MutableLiveData<ImageResponse> {
        val getMovieImagesLiveData = MutableLiveData<ImageResponse>()
        viewModelScope.launch {
            val respuesta = repositorio.getMovieImages(movieId)
            if (respuesta.code() == 200) {
                val addFavorite = respuesta.body()
                addFavorite.let {
                    getMovieImagesLiveData.postValue(it)

                }
            }
        }
        return getMovieImagesLiveData
    }

    fun getSerieImages(serieId: Int): MutableLiveData<ImageResponse> {
        val getSerieImagesLiveData = MutableLiveData<ImageResponse>()
        viewModelScope.launch {
            val respuesta = repositorio.getSerieImages(serieId)
            if (respuesta.code() == 200) {
                val addFavorite = respuesta.body()
                addFavorite.let {
                    getSerieImagesLiveData.postValue(it)
                }
            }
        }
        return getSerieImagesLiveData
    }

    fun getMovieById(movieId: Int, language: String): MutableLiveData<MovieDetallesResponse?> {
        val getMovieByIdLiveData = MutableLiveData<MovieDetallesResponse?>()
        viewModelScope.launch {
            val respuesta = repositorio.getMovieById(movieId, language)
            if (respuesta.code() == 200) {
                val addFavorite = respuesta.body()
                getMovieByIdLiveData.postValue(addFavorite)
            }
        }
        return getMovieByIdLiveData
    }

    fun getSerieById(serieId: Int, language: String): MutableLiveData<SerieDetallesResponse?> {
        val getSerieByIdLiveData = MutableLiveData<SerieDetallesResponse?>()
        viewModelScope.launch {
            val respuesta = repositorio.getSerieById(serieId, language)
            if (respuesta.code() == 200) {
                val getSerie = respuesta.body()
                getSerieByIdLiveData.postValue(getSerie)
            }
        }

        return getSerieByIdLiveData
    }

    //Guardar película al cambio de pantalla
    fun setPelicula(pelicula: MovieDetallesResponse) {
        peliculaLivedata.value = pelicula
    }

    fun getPelicula() = peliculaLivedata


    //Guardar TVShow al cambio de pantalla
    fun setSerie(serie: SerieDetallesResponse) {
        serieLiveData.value = serie
    }

    fun getSerie() = serieLiveData


    fun getFavoriteMovies(acountId: Int): MutableLiveData<List<Movie>> {
        viewModelScope.launch {
            val respuesta = repositorio.getFavoriteMovies(acountId)
            if (respuesta.code() == 200) {
                val listaMoviesFav = respuesta.body()
                listaMoviesFav?.let {
                    listaFavMovies.postValue(it.results)
                }
            }
        }
        return listaFavMovies
    }

    fun getInfoMovie(timeWindow: String = "day"): MutableLiveData<List<Movie>> {
        viewModelScope.launch {
            val respuesta = repositorio.getTrendingMovies(timeWindow)
            if (respuesta.code() == 200) {
                val movieTrending = respuesta.body()
                movieTrending?.let {
                    listaMovies.postValue(it.results)
                }
            }
        }
        return listaMovies
    }

    fun getInfoPeople(timeWindow: String = "day"): MutableLiveData<List<People>> {
        viewModelScope.launch {
            val respuesta = repositorio.getTrendingPeople(timeWindow)
            if (respuesta.code() == 200) {
                val peopleTrending = respuesta.body()
                peopleTrending?.let {
                    actorInfo.postValue(it.results)
                }
            }
        }

        return actorInfo
    }


    fun getMovieBuscador(buscador : String) : MutableLiveData<List<Movie>>{
        viewModelScope.launch {
            val respuesta = repositorio.getBuscadorMovie(buscador)
            if (respuesta.code() == 200){
                val movieBuscador = respuesta.body()
                movieBuscador?.let {
                    listaMoviesBuscador.postValue(it.results)
                }
            }
        }

        return listaMoviesBuscador
    }

    fun getSerieBuscador(buscador : String) : MutableLiveData<List<TVShow>>{
        viewModelScope.launch {
            val respuesta = repositorio.getBuscarSerie(buscador)
            if (respuesta.code() == 200){
                val serieBuscador = respuesta.body()
                serieBuscador?.let {
                    listaShowBuscador.postValue(it.results)
                }
            }
        }

        return listaShowBuscador
    }


    fun setBuscador(lista : List<Movie>){
        listaBuscadorLiveData.value = lista
    }

    fun setBuscadorSerie(lista : List<TVShow>){
        listaBuscadorSerieLiveData.value = lista
    }

    fun getBuscadorPeliculas() = listaMoviesBuscador

    fun getBuscadorSeries() = listaBuscadorSerieLiveData
    fun getFavMovies() = listaFavMovies

    fun getFavoriteTVShows(acountId: Int): MutableLiveData<List<TVShow>> {
        viewModelScope.launch {
            val respuesta = repositorio.getFavoriteTVShows(acountId)
            if (respuesta.code() == 200) {
                val listaTVshowFav = respuesta.body()
                listaTVshowFav?.let {
                    listaFavSeries.postValue(it.results)
                }
            }
        }
        return listaFavSeries
    }


    fun getFavTVShows() = listaFavSeries

    fun getFavoriteWatchListMovies(accountId: Int): MutableLiveData<List<Movie>>{
        viewModelScope.launch {
            val respuesta = repositorio.getFavouriteWatchListMovies(accountId)
            if (respuesta.code() == 200){
                val response = respuesta.body()
                response?.let {
                    listaFavMovies.postValue(it.results)
                }
            }
        }

        return listaMovies
    }

    fun getFavouriteWatchListTVShows(accountId: Int): MutableLiveData<List<TVShow>> {
        //val listaWlTVshowsFavLiveData = MutableLiveData<List<TVShow>>()
        viewModelScope.launch {
            val respuesta = repositorio.getFavouriteWatchListTVShows(accountId)
            if (respuesta.code() == 200) {
                val response = respuesta.body()
                response?.let {

                    listaWLTVShowsFav.postValue(it.results)
                }

            }
        }
        return listaWLTVShowsFav
    }

    fun setAccountId(id: Int) {
        accountID.value = id
    }
    // Devuelve la ID de la cuenta

    fun getUserType() = userCategory

    fun setUserType(user: String) {
        userCategory.value = user
    }

    // Devuelve la ID de la cuenta
    fun getAccountID(sessionID: String): MutableLiveData<Int> {
        viewModelScope.launch {
            val response = repositorio.getAccountDetails(sessionID)
            if (response.code() == 200) {
                response.body()?.id.let {
                    accountID.postValue(it)
                }
            }
        }
        return accountID
    }
}

