package com.sixt.task.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sixt.task.R
import com.sixt.task.model.vo.Car
import com.squareup.picasso.Picasso

class CarListAdapter: RecyclerView.Adapter<CarListAdapter.CarListViewHolder>() {

    private val list = arrayListOf<Car>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CarListViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        val item = list[position]
        holder.model.text = item.model
        holder.make.text = item.make
        holder.licensePlate.text = item.licensePlate
        holder.transmissionType.text = item.transmission
        holder.fuelType.text = item.fuelType

        // holder.root.setOnClickListener { listener?.onItemClick(item) }

        if (item.carImageUrl.isNullOrEmpty()) {
            // TODO: loadPlaceHolder
            // holder.image.setImageResource()
        } else {
            Picasso.get()
                .load(item.carImageUrl)
                .placeholder(R.drawable.car_img_placeholder)
                .error(R.drawable.car_img_placeholder)
                .into(holder.image)
        }
    }

    fun setList(list: List<Car>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class CarListViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val root: View = itemView.findViewById(R.id.rootView)
        val image: ImageView = itemView.findViewById(R.id.carImageView)
        val model: TextView = itemView.findViewById(R.id.modelTextView)
        val make: TextView = itemView.findViewById(R.id.makeTextView)
        val licensePlate: TextView = itemView.findViewById(R.id.licencePlateTextView)
        val transmissionType: TextView = itemView.findViewById(R.id.transmissionTextView)
        val fuelType: TextView = itemView.findViewById(R.id.fuelTextView)
    }
}