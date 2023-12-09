package bloomand.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bloomand.myapplication.databinding.DishItemBinding

class DishAdapter(val listener: Listener): RecyclerView.Adapter<DishAdapter.DishHolder>(){
    val dishList = ArrayList<Dish>()

    class DishHolder(item:View): RecyclerView.ViewHolder(item){
        private val imageIdList = listOf(
            R.drawable.dish1,
            R.drawable.dish2,
            R.drawable.dish3,
            R.drawable.dish4)

        val binding = DishItemBinding.bind(item)
        fun bind(dish: Dish, listener: Listener) = with(binding){
            im.setImageResource(imageIdList[dish.imageID])
            name.text = dish.name
            itemView.setOnClickListener{
                listener.onClick(dish)
            }
            itemView.setOnLongClickListener{
                listener.onLongClick(dish)
                true
            }
        }
    }

    interface Listener {
        fun onClick(dish: Dish)
        fun onLongClick(dish: Dish)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        return DishHolder(view)
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    override fun onBindViewHolder(holder: DishHolder, position: Int) {
        holder.bind(dishList[position], listener)
    }

    fun addDish(dish: Dish){
        dishList.add(dish)
    }
}