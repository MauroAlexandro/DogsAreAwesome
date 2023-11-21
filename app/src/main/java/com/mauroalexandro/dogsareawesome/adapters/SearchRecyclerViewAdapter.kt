package com.mauroalexandro.dogsareawesome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mauroalexandro.dogsareawesome.R
import com.mauroalexandro.dogsareawesome.models.Breed
import java.util.Locale

class SearchRecyclerViewAdapter(private var dogsList: ArrayList<Breed>, private val context: Context) :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>(), Filterable {

    var dogsFilteredList = ArrayList<Breed>()

    init {
        dogsFilteredList = dogsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.search_result_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(searchViewHolder: SearchViewHolder, position: Int) {
        if(!dogsFilteredList[position].name.isNullOrEmpty()) {
            searchViewHolder.breedName.text = dogsFilteredList[position].name
        } else {
            searchViewHolder.breedName.text = context.getString(R.string.detail_fragment_no_data)
        }
        if(!dogsFilteredList[position].breed_group.isNullOrEmpty()) {
            searchViewHolder.breedGroup.text = dogsFilteredList[position].breed_group
        } else {
            searchViewHolder.breedGroup.text = context.getString(R.string.detail_fragment_no_data)
        }
        if(!dogsFilteredList[position].origin.isNullOrEmpty()) {
            searchViewHolder.breedOrigin.text = dogsFilteredList[position].origin
        } else {
            searchViewHolder.breedOrigin.text = context.getString(R.string.detail_fragment_no_data)
        }
    }

    override fun getItemCount(): Int {
        return dogsFilteredList.size
    }
    fun getItemAtPosition(position: Int): Breed {
        return dogsFilteredList[position]
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                dogsFilteredList = if (charSearch.isEmpty()) {
                    dogsList
                } else {
                    val resultList = ArrayList<Breed>()
                    for (row in dogsList) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dogsFilteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dogsFilteredList = results?.values as ArrayList<Breed>
                notifyDataSetChanged()
            }
        }
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val breedName: TextView = view.findViewById(R.id.dog_search_name)
        val breedGroup: TextView = view.findViewById(R.id.dog_search_group)
        val breedOrigin: TextView = view.findViewById(R.id.dog_search_origin)
    }
}