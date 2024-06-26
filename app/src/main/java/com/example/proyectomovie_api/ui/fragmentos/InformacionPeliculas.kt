package com.example.proyectomovie_api.ui.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.proyectomovie_api.data.favorite.addFavoriteBody
import com.example.proyectomovie_api.data.movie.Movie
import com.example.proyectomovie_api.data.movie_detalles.MovieDetallesResponse
import com.example.proyectomovie_api.databinding.FragmentInformacionPeliculasBinding
import com.example.proyectomovie_api.ui.MainActivity
import com.example.proyectomovie_api.ui.view.MyViewModel
import com.example.proyectomovie_api.data.watchlist.addWatchListBody
import com.example.proyectomovie_api.ui.carousel.ImagenCarousel
import com.example.proyectomovie_api.ui.carousel.ImagenCarouselAdaptador
import com.example.proyectomovie_api.ui.carousel.ImagenCarouselAdaptadorInformacion
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class InformacionPeliculas : Fragment() {

    private lateinit var binding: FragmentInformacionPeliculasBinding
    private val viewModel by activityViewModels<MyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformacionPeliculasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserType().observe(viewLifecycleOwner){
            if (it == "Invitado"){
                binding.floatingbtnWatchListDetallesPelicula.visibility = View.GONE
                binding.floatingbtnMiListaDetallesPelicula.visibility = View.GONE
            }
        }

        viewModel.getPelicula().observe(viewLifecycleOwner){ movie ->
            rellenaDatos(movie)

            movie.id?.let { viewModel.getMovieImages(it).observe(viewLifecycleOwner){ it2 ->
                val listaURLs = it2?.backdrops?.mapIndexed{index, backdrop ->
                    ImagenCarousel(index,"https://image.tmdb.org/t/p/original${backdrop.file_path}" )
                } ?: emptyList()
                    binding.recyclerViewDetallesPelicula.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.recyclerViewDetallesPelicula.adapter = ImagenCarouselAdaptadorInformacion(listaURLs)
                }
            }

            binding.floatingbtnWatchListDetallesPelicula.setOnClickListener {
                viewModel.getSessionID().observe(viewLifecycleOwner) { sessionId ->
                    viewModel.getAccountID(sessionId).observe(viewLifecycleOwner) { accountId ->
                        val data = movie.id?.let { it1 -> addWatchListBody("movie", it1, true) }
                        if (data != null) {
                            viewModel.addToWatchList(accountId, data).observe(viewLifecycleOwner) {
                                val snackbarPositiva = Snackbar.make(binding.root, "Pelicula añadida a tu watchlist", Snackbar.LENGTH_SHORT)
                                val snackbarNegativa = Snackbar.make(binding.root, "Error", Snackbar.LENGTH_SHORT)
                                if (it.success) {
                                    snackbarPositiva.show()
                                } else {
                                    snackbarNegativa.show()
                                }
                            }
                        }
                    }
                }
            }

            binding.floatingbtnMiListaDetallesPelicula.setOnClickListener {
                viewModel.getSessionID().observe(viewLifecycleOwner){ sessionId ->
                    viewModel.getAccountID(sessionId).observe(viewLifecycleOwner){accountId ->
                        val data = movie.id?.let { it1 -> addFavoriteBody("movie", it1, true) }
                        viewModel.getFavoriteMovies(accountId).observe(viewLifecycleOwner) { lista ->
                            var encontrado = false
                            lista.forEach { objeto ->
                                if (objeto.id == data?.media_id) {
                                    encontrado = true
                                }
                            }
                            if (encontrado) {
                                val snackbar = Snackbar.make(binding.root,"Ya tienes esta película en favoritos",Snackbar.LENGTH_SHORT)
                                snackbar.show()
                            } else {
                                if (data != null) {
                                    viewModel.addToFavorite(requireContext(),accountId, data).observe(viewLifecycleOwner) {
                                        val snackbarPositiva = Snackbar.make(binding.root,"Pelicula añadida a tus favoritos", Snackbar.LENGTH_SHORT)
                                        val snackbarNegativa = Snackbar.make(binding.root,"Error", Snackbar.LENGTH_SHORT)

                                        if (it.success) {
                                            snackbarPositiva.show()
                                        } else {
                                            snackbarNegativa.show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            movie.id?.let {
                viewModel.getMovieWatchProvider(it).observe(viewLifecycleOwner){ provider ->
                    binding.tvProviderDetallesPelicula.text = "No está disponible en servicios de streaming"
                    provider.results?.let { providerResult ->
                        providerResult.ES?.let {spain ->
                            spain.buy?.let {compra ->
                            binding.tvProviderDetallesPelicula.text = "Disponible en: " + compra.first().provider_name
                            }
                        }
                    }
                }
            }
        }
    }

    private fun rellenaDatos(peli: MovieDetallesResponse) {
        val originalURL = "https://media.themoviedb.org/t/p/original" + peli.backdropPath
        val posterURL = "https://media.themoviedb.org/t/p/w300_and_h450_bestv2" + peli.posterPath
        val backgroundURL = "https://media.themoviedb.org/t/p/w1920_and_h800_multi_faces"

        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_UP
        val ratingRedondeado = df.format(peli.voteAverage)

        with(binding) {

            tvTituloDetallesPelicula.text = peli.title
            tvRateDetallesPelicula.text = "Valoración: " + ratingRedondeado.toString() + " / 10"
            tvReleaseDateDetallesPelicula.text = peli.releaseDate

            Glide.with(requireContext())
                .load(posterURL)
                .into(binding.ivCartelDetallesPelicula)

            Glide.with(requireContext())
                .load(originalURL)
                .into(binding.ivFondoDetallesPelicula)

            tvGenresDetallesPelicula.text = peli.genres?.get(0)?.name.toString()
            tvOriginCountryDetallesPelicula.text = peli.originCountry?.get(0).toString() + " · "
            tvDuracionDetallesPelicula.text = peli.runtime.toString() + " min"
            tvOverviewDetallesPelicula.text = peli.overview

            (requireActivity() as MainActivity).supportActionBar?.setTitle(peli.title)
        }
    }
}