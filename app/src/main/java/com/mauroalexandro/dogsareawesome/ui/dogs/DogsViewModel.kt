package com.mauroalexandro.dogsareawesome.ui.dogs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mauroalexandro.dogsareawesome.models.Breed
import com.mauroalexandro.dogsareawesome.models.BreedImage
import com.mauroalexandro.dogsareawesome.network.ApiClient
import com.mauroalexandro.dogsareawesome.network.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogsViewModel : ViewModel() {
    init {
        getBreedsFromService()
    }

    private val getBreeds = MutableLiveData<Resource<ArrayList<Breed>>>()
    private val getBreedImagesByID = MutableLiveData<Resource<BreedImage>>()

    private var apiClient: ApiClient = ApiClient.getInstance()

    private var breedScope: Job? = null
    private var breedImagesScope: Job? = null

    /**
     * GET Breeds
     */
    private fun getBreedsFromService() {
        breedScope = CoroutineScope(Dispatchers.IO).launch {
            getBreeds.postValue(Resource.loading(null))

            apiClient.getClient()?.getBreeds()?.enqueue(object :
                Callback<ArrayList<Breed>?> {
                override fun onResponse(
                    call: Call<ArrayList<Breed>?>,
                    response: Response<ArrayList<Breed>?>
                ) {
                    val breedResponse: ArrayList<Breed>? = response.body()

                    if (breedResponse != null) {
                        getBreeds.postValue(Resource.success(breedResponse))
                    }
                }

                override fun onFailure(call: Call<ArrayList<Breed>?>, t: Throwable) {
                    Log.e("ERROR", "Error: " + t.message)
                    getBreeds.postValue(t.message?.let { Resource.error(it, null) })
                    call.cancel()
                }
            })
        }
    }

    fun getImagesOfReceivedBreeds(breedArrayList: ArrayList<Breed>) {
        for (i in breedArrayList) {
            getBreedImagesByIDFromService(i.reference_image_id)
        }
    }

    /**
     * GET Breed Image
     */
    private fun getBreedImagesByIDFromService(referenceImageId: String) {
        breedImagesScope = CoroutineScope(Dispatchers.IO).launch {
            getBreedImagesByID.postValue(Resource.loading(null))

            apiClient.getClient()?.getBreedImageByID(referenceImageId)?.enqueue(object :
                Callback<BreedImage> {
                override fun onResponse(
                    call: Call<BreedImage>,
                    response: Response<BreedImage>
                ) {
                    val breedImageResponse: BreedImage? = response.body()

                    if (breedImageResponse != null) {
                        getBreedImagesByID.postValue(Resource.success(breedImageResponse))
                    }
                }

                override fun onFailure(call: Call<BreedImage>, t: Throwable) {
                    Log.e("ERROR", "Error: " + t.message)
                    getBreedImagesByID.postValue(t.message?.let { Resource.error(it, null) })
                    call.cancel()
                }
            })
        }
    }

    override fun onCleared() {
        super.onCleared()
        breedScope?.cancel()
        breedImagesScope?.cancel()
    }

    fun getBreeds(): MutableLiveData<Resource<ArrayList<Breed>>> {
        return getBreeds
    }

    fun getBreedImagesByID(): MutableLiveData<Resource<BreedImage>> {
        return getBreedImagesByID
    }
}