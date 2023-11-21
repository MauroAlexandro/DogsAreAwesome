package com.mauroalexandro.dogsareawesome.ui.dogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mauroalexandro.dogsareawesome.adapters.DogBreedsRecyclerViewAdapter
import com.mauroalexandro.dogsareawesome.databinding.FragmentDogsBinding
import com.mauroalexandro.dogsareawesome.models.Breed
import com.mauroalexandro.dogsareawesome.models.BreedImage
import com.mauroalexandro.dogsareawesome.network.Status
import com.mauroalexandro.dogsareawesome.ui.details.DetailsFragment
import com.mauroalexandro.dogsareawesome.utils.Utils


class DogsFragment : Fragment() {

    private var _binding: FragmentDogsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dogsViewModel: DogsViewModel
    private lateinit var dogbreedsRecyclerViewAdapter: DogBreedsRecyclerViewAdapter
    private var breeds: ArrayList<Breed> = ArrayList()
    private var breedsImages: ArrayList<BreedImage> = ArrayList()
    private var firstElement = 0
    private var lastElement = 10
    private var listSize = 0
    private var utils: Utils = Utils.getInstance()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dogsViewModel = ViewModelProvider(this)[DogsViewModel::class.java]
        dogbreedsRecyclerViewAdapter = DogBreedsRecyclerViewAdapter(requireContext())
        _binding = FragmentDogsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setBreeds()
        setBreedImages()

        //Switch between Grid and List
        binding.dogbreedsListGridSwitch.setOnCheckedChangeListener { _, checked ->
            when {
                checked -> {
                    binding.dogbreedsRecyclerview.layoutManager = LinearLayoutManager(activity)
                    (binding.dogbreedsRecyclerview.adapter as DogBreedsRecyclerViewAdapter).notifyDataSetChanged()
                }
                else -> {
                    binding.dogbreedsRecyclerview.layoutManager = GridLayoutManager(context, 2)
                    (binding.dogbreedsRecyclerview.adapter as DogBreedsRecyclerViewAdapter).notifyDataSetChanged()
                }
            }
        }

        //Switch to Order Alphabetically
        binding.dogbreedsAlphaSwitch.setOnCheckedChangeListener { _, checked ->
            dogbreedsRecyclerViewAdapter.sortByNameOrGroup(checked)
        }

        binding.dogbreedsRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    //Reached the BOTTOM of the List
                    firstElement += 10
                    lastElement += 10
                    updateAdapter()
                }
            }
        })

        binding.dogbreedsRecyclerview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            var downTouch = false
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> downTouch = true
                    MotionEvent.ACTION_UP -> if (downTouch) {
                        downTouch = false
                        binding.dogbreedsRecyclerview.findChildViewUnder(e.x, e.y)?.let {
                            val position = recyclerView.getChildAdapterPosition(it)
                            val breedClicked = (recyclerView.adapter as DogBreedsRecyclerViewAdapter).getItemAtPosition(position)
                            activity?.let { fragmentActivity -> utils.openDetailsFragment(fragmentActivity, breedClicked) }
                        }
                    }
                    else -> downTouch = false
                }
                return super.onInterceptTouchEvent(recyclerView, e)
            }
        })

        return root
    }

    override fun onAttach(context: Context) {
        firstElement = 0
        lastElement = 10
        super.onAttach(context)
    }

    private fun setBreeds() {
        //Get Breeds
        activity?.let { it ->
            dogsViewModel.getBreeds().observe(it) {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.dogbreedsProgressBar.visibility = View.GONE

                        it.data?.let { breeds ->
                            if(breeds.isNotEmpty()) {
                                this.breeds = breeds
                                listSize = this.breeds.size
                                lastElement = listSize - 1
                                updateAdapter()
                                utils.setList(utils.breedListKey,breeds,requireContext())

                                //Calls Breed Images Service
                                dogsViewModel.getImagesOfReceivedBreeds(breeds)

                                binding.dogbreedsRecyclerview.visibility = View.VISIBLE
                                binding.dogbreedsRecyclerview.adapter = dogbreedsRecyclerViewAdapter
                                binding.dogbreedsRecyclerview.layoutManager = LinearLayoutManager(activity)
                            } else {
                                binding.dogbreedsRecyclerview.visibility = View.GONE
                                Toast.makeText(activity, "No results found!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    Status.LOADING -> {
                        binding.dogbreedsProgressBar.visibility = View.VISIBLE
                        binding.dogbreedsRecyclerview.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        binding.dogbreedsProgressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setBreedImages() {
        //Get Breed Images
        activity?.let { it ->
            dogsViewModel.getBreedImagesByID().observe(it) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { breedImage ->
                            if(!breedsImages.contains(breedImage)){
                                breedsImages.add(breedImage)
                                dogbreedsRecyclerViewAdapter.setBreedImages(breedsImages)

                                val breed = breeds.find { it.reference_image_id == breedImage.id }
                                if (breed != null) {
                                    dogbreedsRecyclerViewAdapter.updateItem(breed)
                                }
                            }
                        }
                    }

                    Status.LOADING -> { }

                    Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun updateAdapter() {
        if(lastElement <= listSize-1 && dogbreedsRecyclerViewAdapter.itemCount < listSize-1) {
            if(firstElement >= lastElement) {
                firstElement = 0
                lastElement = listSize - 1
            }

            if(listSize <= dogbreedsRecyclerViewAdapter.itemCount + 10)
                lastElement = listSize-1

            val sublist = ArrayList(breeds.subList(firstElement, lastElement))
            dogbreedsRecyclerViewAdapter.setBreedList(sublist)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}