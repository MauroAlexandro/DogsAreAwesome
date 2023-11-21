package com.mauroalexandro.dogsareawesome.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mauroalexandro.dogsareawesome.R
import com.mauroalexandro.dogsareawesome.databinding.FragmentDetailsBinding
import com.mauroalexandro.dogsareawesome.models.Breed

class DetailsFragment(private val breed: Breed) : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Fill data detail from this breed
        if(!breed.name.isNullOrEmpty()) {
            binding.dogDetailName.text = breed.name
        }
        if(!breed.breed_group.isNullOrEmpty()) {
            binding.dogDetailGroup.text = breed.breed_group
        }
        if(!breed.origin.isNullOrEmpty()) {
            binding.dogDetailOrigin.text = breed.origin
        }
        if(!breed.temperament.isNullOrEmpty()) {
            binding.dogDetailTemperment.text = breed.temperament
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}