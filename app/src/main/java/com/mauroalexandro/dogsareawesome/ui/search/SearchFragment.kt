package com.mauroalexandro.dogsareawesome.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mauroalexandro.dogsareawesome.adapters.SearchRecyclerViewAdapter
import com.mauroalexandro.dogsareawesome.databinding.FragmentSearchBinding
import com.mauroalexandro.dogsareawesome.utils.Utils

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var utils: Utils = Utils.getInstance()
    private lateinit var searchRecyclerViewAdapter: SearchRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val breedList = utils.getList(utils.breedListKey)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        searchRecyclerViewAdapter = SearchRecyclerViewAdapter(breedList as ArrayList, requireContext())
        binding.dogbreedsSearchRecyclerview.visibility = View.VISIBLE
        binding.dogbreedsSearchRecyclerview.adapter = searchRecyclerViewAdapter
        binding.dogbreedsSearchRecyclerview.layoutManager = LinearLayoutManager(activity)

        binding.dogsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRecyclerViewAdapter.filter.filter(newText)
                return false
            }

        })

        binding.dogbreedsSearchRecyclerview.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            var downTouch = false
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> downTouch = true
                    MotionEvent.ACTION_UP -> if (downTouch) {
                        downTouch = false
                        binding.dogbreedsSearchRecyclerview.findChildViewUnder(e.x, e.y)?.let {
                            val position = recyclerView.getChildAdapterPosition(it)
                            val breedClicked = (recyclerView.adapter as SearchRecyclerViewAdapter).getItemAtPosition(position)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}