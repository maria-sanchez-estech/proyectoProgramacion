package com.example.proyectomovie_api.ui.adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomovie_api.databinding.MiListaPeliculasHolderBinding
import com.example.proyectomovie_api.ui.carousel.ImagenCarousel

class AdaptadorCarouselSeries (val listado: ArrayList<String>, val listener: MyClick)  : RecyclerView.Adapter<AdaptadorCarouselSeries.vistaCelda>() {

    //Este adaptador hace que funcione el carusel orincipal de series

    inner class vistaCelda(val binding: MiListaPeliculasHolderBinding ) : RecyclerView.ViewHolder(binding.root){

    }

    interface MyClick {
        fun onHolderClick(imagenCarousel: ImagenCarousel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vistaCelda {
        return vistaCelda(MiListaPeliculasHolderBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun getItemCount(): Int {
        return listado.size
    }

    override fun onBindViewHolder(holder: vistaCelda, position: Int) {
        val nombre = listado[position]


    }
}