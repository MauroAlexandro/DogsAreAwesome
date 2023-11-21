package com.mauroalexandro.dogsareawesome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mauroalexandro.dogsareawesome.R
import com.mauroalexandro.dogsareawesome.models.Breed
import com.mauroalexandro.dogsareawesome.models.BreedImage

class DogBreedsRecyclerViewAdapter(
    private val context: Context
) : RecyclerView.Adapter<DogBreedsRecyclerViewAdapter.BreedsViewHolder>() {
    private var breedList = arrayListOf<Breed>()
    private var breedsImagesList = arrayListOf<BreedImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedsViewHolder {
        return BreedsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.breed_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(breedViewHolder: BreedsViewHolder, position: Int) {
        //Breed
        val breed: Breed = getBreedList(position)

        //Breed Name
        breedViewHolder.breedName.text = breed.name

        //Breed Image
        val breedImage = breedsImagesList.find { it.id == breed.reference_image_id }
        if(breedImage != null) {
            Glide
                .with(context)
                .load(breedImage.url)
                .centerCrop()
                .apply(RequestOptions().override(500, 500))
                .into(breedViewHolder.breedImage)
        } else {
            Glide
                .with(context)
                .load(R.drawable.no_image)
                .centerCrop()
                .apply(RequestOptions().override(500, 500))
                .into(breedViewHolder.breedImage)
        }
    }

    override fun getItemCount(): Int {
        return breedList.size
    }
    private fun getItemPosition(breed: Breed): Int {
        return breedList.indexOfFirst { it.id == breed.id }
    }

    private fun getBreedList(position: Int): Breed {
        return breedList[position]
    }

    fun getItemAtPosition(position: Int): Breed {
        return breedList[position]
    }

    fun setBreedList(breedList1: List<Breed>) {
        if(this.breedList.isEmpty())
            this.breedList = breedList1 as ArrayList<Breed>
        else
            this.breedList.addAll(breedList1)
        notifyDataSetChanged()
    }

    fun setBreedImages(breedsImages: ArrayList<BreedImage>) {
        this.breedsImagesList = breedsImages
    }

    fun updateItem(breed: Breed) {
        val itemPosition = getItemPosition(breed)
        notifyItemChanged(itemPosition)
    }

    fun sortByNameOrGroup(checked: Boolean) {
        if(checked) {
            breedList.sortBy { it.name }
        } else {
            breedList.sortBy { it.breed_group }
        }
        notifyDataSetChanged()
    }

    class BreedsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val breedName: TextView = view.findViewById(R.id.breed_name_row)
        val breedImage: ImageView = view.findViewById(R.id.breed_image_row)
    }
}