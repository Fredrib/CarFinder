package com.sixt.task.view.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sixt.task.R
import com.sixt.task.viewmodel.CarViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BottomSheetFragment: BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = BottomSheetFragment()
    }

    private val viewModel by sharedViewModel<CarViewModel>()

    private lateinit var modelTV: TextView
    private lateinit var makeTV: TextView
    private lateinit var plateTV: TextView
    private lateinit var transmissionTV: TextView
    private lateinit var fuelTypeTV: TextView
    private lateinit var tankTV: TextView
    private lateinit var colorTV: TextView
    private lateinit var cleanlinessTV: TextView

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_detail, container, false)

        modelTV = view.findViewById(R.id.modelTextView)
        makeTV = view.findViewById(R.id.makeTextView)
        plateTV = view.findViewById(R.id.licencePlateTextView)
        transmissionTV = view.findViewById(R.id.transmissionTextView)
        fuelTypeTV = view.findViewById(R.id.fuelTextView)
        tankTV = view.findViewById(R.id.tankTextView)
        colorTV = view.findViewById(R.id.colorTextView)
        cleanlinessTV = view.findViewById(R.id.cleanlinessTextView)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadSelection()

        viewModel.selectedCar().observe(this, Observer {car ->
            modelTV.text = car.model
            makeTV.text = car.make
            plateTV.text = car.plate
            transmissionTV.text = car.transmission
            fuelTypeTV.text = car.fuelType
            tankTV.text = car.fuelLevel
            colorTV.text = car.color
            cleanlinessTV.text = car.cleanliness
        })
    }
}