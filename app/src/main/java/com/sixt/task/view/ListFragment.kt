package com.sixt.task.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.sixt.task.R
import com.sixt.task.model.Resource
import com.sixt.task.model.vo.Car
import com.sixt.task.viewmodel.CarViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private val viewModel by viewModel<CarViewModel>()
    private val adapter = CarListAdapter()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.list_fragment, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadData()

        viewModel.getCars().observe(this, Observer<Resource<List<Car>>> { resource ->
            resource?.let {
                when (resource) {
                    is Resource.Loading -> Log.e("ListFragment", "showing loading")
                    is Resource.Error -> Log.e("ListFragment", "showing error")
                    is Resource.Success -> it.data?.let { list -> adapter.setList(list) }
                }
            }
        })
    }
}
