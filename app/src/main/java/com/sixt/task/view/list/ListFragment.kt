package com.sixt.task.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sixt.task.R
import com.sixt.task.model.Resource
import com.sixt.task.model.vo.CarDTO
import com.sixt.task.model.vo.CarVO
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
    private lateinit var errorMessage: TextView
    private lateinit var tryAgainButton: Button

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

        errorMessage = view.findViewById(R.id.errorMessage)

        tryAgainButton = view.findViewById(R.id.tryAgainBtn)
        tryAgainButton.setOnClickListener { viewModel.loadData() }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadData()

        viewModel.cars().observe(this, Observer<Resource<List<CarDTO>>> { resource ->
            resource?.let {
                when (resource) {
                    is Resource.Loading -> {
                        clearErrors()
                        progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Error -> handleError(it.message)
                    is Resource.Success -> {
                        progressBar.visibility = View.GONE
                        clearErrors()
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

    override fun onSelect(car: CarDTO) {
        viewModel.selectCar(car)
        activity?.finish()
    }

    private fun handleError(message: String?) {
        progressBar.visibility = View.GONE
        errorMessage.visibility = View.VISIBLE
        tryAgainButton.visibility = View.VISIBLE
        message?.let { it -> showError(it) }
    }

    private fun showError(message: String) {
        view?.let {
            Snackbar
                .make(it, message , Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun clearErrors() {
        errorMessage.visibility = View.GONE
        tryAgainButton.visibility = View.GONE
    }
}
