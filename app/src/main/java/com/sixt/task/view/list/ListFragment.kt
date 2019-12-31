package com.sixt.task.view.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sixt.task.R
import com.sixt.task.model.Resource
import com.sixt.task.model.vo.Car
import com.sixt.task.viewmodel.CarViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListFragment : Fragment(), CarListAdapter.SelectionListener{

    companion object {
        fun newInstance() = ListFragment()
    }

    private val viewModel by sharedViewModel<CarViewModel>()
    private val adapter = CarListAdapter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.list_fragment, container, false)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        recyclerView.addItemDecoration(itemDecoration)

        progressBar = view.findViewById(R.id.progressBar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadData()

        viewModel.cars().observe(this, Observer<Resource<List<Car>>> { resource ->
            resource?.let {
                when (resource) {
                    is Resource.Loading -> progressBar.visibility = View.VISIBLE
                    is Resource.Error -> Log.e("ListFragment", "showing error")
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { list -> adapter.setList(list) }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.registerSelectionListener(this)
    }

    override fun onStop() {
        adapter.unregisterSelectionListener()
        super.onStop()
    }

    override fun onSelect(car: Car) {
        viewModel.selectCar(car)
        activity?.finish()
    }
}
