package bloomand.myapplication.ui.liked

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bloomand.myapplication.DbHelper
import bloomand.myapplication.Dish
import bloomand.myapplication.DishAdapter

import bloomand.myapplication.databinding.FragmentLikedBinding
import bloomand.myapplication.databinding.FragmentSearchBinding
import bloomand.myapplication.dishes
import bloomand.myapplication.ui.current.CurrentFragment


lateinit var liked_dishes: List<Dish>

class LikedFragment : Fragment(), DishAdapter.Listener {

    private var _binding: FragmentLikedBinding? = null
    private val adapter = DishAdapter(this)

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLikedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }

    private fun init(){
        val email = arguments?.getString("email")

        binding.apply {
            rcViewLiked.layoutManager = LinearLayoutManager(view?.context)
            rcViewLiked.adapter = adapter
            val db = context?.let { DbHelper(it) }
            val userId = db?.getUserID(email.toString())
            liked_dishes = userId?.let { db?.getFavDish(it) }!!
            for(i in liked_dishes){
                adapter.addDish(i)
            }
            Toast.makeText(context,"Done", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(dish: Dish) {
        val email = arguments?.getString("email")
        val bundle = Bundle()
        bundle.putInt("id", dish.id)
        bundle.putString("name", dish.name)
        bundle.putInt("calorie", dish.calorie)
        bundle.putInt("time", dish.time)
        bundle.putInt("difficulty", dish.difficulty)
        bundle.putString("ingredient", dish.ingredient)
        bundle.putInt("imageID", dish.imageID)
        bundle.putString("email", email)


        val transaction = parentFragmentManager.beginTransaction()
        val fragment = CurrentFragment()
        fragment.arguments = bundle
        transaction.replace(bloomand.myapplication.R.id.container,fragment)
        transaction.commit()
    }

    override fun onLongClick(dish: Dish) {
        TODO("Not yet implemented")
    }
}