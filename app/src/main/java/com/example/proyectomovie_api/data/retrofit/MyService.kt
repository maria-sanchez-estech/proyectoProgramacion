package com.example.proyectomovie_api.data.retrofit

//import com.example.proyectomovie_api.data.inicioSesion.BodyLogin
//import com.example.proyectomovie_api.data.inicioSesion.CreateSessionResponse
//import com.example.proyectomovie_api.data.inicioSesion.RequestTokenResponse
import com.example.proyectomovie_api.watchlist.addWatchListBody
import com.example.proyectomovie_api.data.movie.MovieResponse
import com.example.proyectomovie_api.data.movieProvider.MovieProviderResponse
//import com.example.proyectomovie_api.data.people.PeopleResponse
import com.example.proyectomovie_api.data.tv.TVResponse
import com.example.proyectomovie_api.data.tvSerieProvider.TVSerieResponse
import com.example.proyectomovie_api.watchlist.WatchListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MyService {

    /*
    *   PETICIONES PARA EL LOGIN
     */


    // Create Request Token
//    @GET("authentication/token/new")
//    suspend fun getAuthToken(
//        @Query("api_key") apiKey:String
//    ):Response<RequestTokenResponse>
//
//    // Entre estas habría que autenticar el token enviando al usuario a la página
//
//
//    //Create Session with Login (username + password + requestToken ya autenticada por el body)
//    @Headers("Content-Type: application/json")
//    @POST("authentication/token/validate_with_login")
//    suspend fun createSessionLogin(
//        @Body body: BodyLogin,
//        @Query("api_key") apiKey:String
//    ):Response<CreateSessionResponse>
//
//    // Create Guest Session
//    @GET("authentication/guest_session/new")
//    suspend fun createGuestSession(
//        @Query("api_key") apiKey:String
//    ):Response<CreateSessionResponse>

    // Lista de descubrimientos de películas
    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun discoverMovies(
        @Query("api_key") apiKey:String
    ): Response<MovieResponse>

    // Lista de descubrimientos de TVShows
    @GET("discover/tv?sort_by=popularity.desc")
    suspend fun discoverTVShows(
        @Query("api_key") apiKey:String
    ): Response<TVResponse>

    // Plataformas en las que se puede ver una peli
    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieProvider(
        @Query("api_key") apiKey:String,
        @Path("movie_id") movieID:Int
    ): Response<MovieProviderResponse>

    // Plataformas en las que se puede ver un TVShow
    @GET("tv/{series_id}/watch/providers")
    suspend fun getTVShowProvider(
        @Query("api_key") apiKey:String,
        @Path("series_id") tvID:Int
    ): Response<TVSerieResponse>


    // Películas favoritas del usuario
    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavMovies(
        @Query("api_key") apiKey:String,
        @Path("account_id") userID: Int
    ): Response<MovieResponse>


    //TVShows favoritos del usuario
    @GET("account/{account_id}/favorite/tv")
    suspend fun getFavTVShows(
        @Query("api_key") apiKey:String,
        @Path("account_id") userID: Int
    ): Response<TVResponse>

    // Películas más populares
    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("api_key") apiKey:String,
        @Query("region") region:String
    ):Response<MovieResponse>

    // Películas mejor valoradas
    @GET("movie/top_rated")
    suspend fun topRatedMovies(
        @Query("api_key") apiKey:String,
        @Query("region") region:String
    ):Response<MovieResponse>

    // TVShows más populares
    @GET("tv/popular")
    suspend fun popularTVShows(
        @Query("api_key") apiKey:String,
        @Query("region") region:String
    ):Response<TVResponse>

    // TVShows mejor valorados
    @GET("tv/top_rated")
    suspend fun topRatedTVShows(
        @Query("api_key") apiKey:String,
        @Query("region") region:String
    ):Response<TVResponse>

    // Personas Trending del día o semana
//    @GET("trending/person/{time_window}")
//    suspend fun trendingPeople(
//        @Query("api_key") apiKey:String,
//        @Path("time_window") timeWindow:String
//    ):Response<PeopleResponse>

    // Peliculas Trending del día o semana
    @GET("trending/movie/{time_window}")
    suspend fun trendingMovies(
        @Query("api_key") apiKey:String,
        @Path("time_window") timeWindow:String
    ):Response<MovieResponse>


    // TVShows Trending del día o semana
    @GET("trending/tv/{time_window}")
    suspend fun trendingTVShows(
        @Query("api_key") apiKey:String,
        @Path("time_window") timeWindow:String
    ):Response<TVResponse>


    // Obtiene la lista de proveedores por país donde la serie está disponible
    @GET("tv/{serie_id}/watch/providers")
    suspend fun getSerieWatchProvider(
        @Query("api_key") apiKey:String,
        @Path("serie_id") movieId: Int
    ):Response<TVSerieResponse>

    // Obtiene la lista de proveedores por país donde la película está disponible
    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProvider(
        @Query("api_key") apiKey: String,
        @Path("movie_id") movieId: Int
    ):Response<MovieProviderResponse>

    @Headers("Content-Type: application/json")
    @POST("account/{account_id}/watchlist")
    suspend fun addToWatchList(
        @Query("api_key") apiKey: String,
        @Path("accoun_id") accountId : Int,
        @Body data : addWatchListBody
    ) : Response<WatchListResponse>

}