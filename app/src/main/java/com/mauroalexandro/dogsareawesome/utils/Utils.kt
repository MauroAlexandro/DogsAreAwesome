package com.mauroalexandro.dogsareawesome.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauroalexandro.dogsareawesome.models.Breed
import com.mauroalexandro.dogsareawesome.ui.details.DetailsFragment
import java.lang.reflect.Type


class Utils {

    companion object {
        private var INSTANCE: Utils? = null
        fun getInstance(): Utils {
            if (INSTANCE == null)
                INSTANCE = Utils()

            return INSTANCE as Utils
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var breedListKey = "BREEDS_LIST"

    /**
     * Convert Breed List into JSON to Save on SharedPreferences
     */
    fun <T> setList(key: String, breedList: List<T>?, context: Context) {
        val gson = Gson()
        val json = gson.toJson(breedList)
        set(key, json, context)
    }
    private operator fun set(key: String?, value: String?, context: Context) {
        sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * Retrieve Breed List from JSON in SharedPreferences, into Arraylist
     */
    fun getList(key: String?): List<Breed> {
        var breedList: List<Breed> = ArrayList()

        if(this::sharedPreferences.isInitialized) {
            val serializedObject: String? = sharedPreferences.getString(key, "")
            if (serializedObject != null) {
                val gson = Gson()
                val type: Type = object : TypeToken<List<Breed?>?>() {}.type
                breedList = gson.fromJson(serializedObject, type)
            }
        }

        return breedList
    }

    /**
     * Show DetailsFragment
     */
    fun openDetailsFragment(activity: FragmentActivity, breed: Breed) {
        val detailsFragment = DetailsFragment(breed)
        if(!activity.supportFragmentManager.isDestroyed)
            detailsFragment.show(
                activity.supportFragmentManager,
                "breed_detail_dialog_fragment"
            )
    }
}