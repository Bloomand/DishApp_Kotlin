package bloomand.myapplication.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bloomand.myapplication.DbHelper
import bloomand.myapplication.Dish
import bloomand.myapplication.DishAdapter
import bloomand.myapplication.databinding.FragmentSearchBinding
import bloomand.myapplication.dishes
import bloomand.myapplication.fetchDishData
import bloomand.myapplication.ui.current.CurrentFragment

class SearchFragment : Fragment(), DishAdapter.Listener {

    private var _binding: FragmentSearchBinding? = null
    private val adapter = DishAdapter(this)

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val dishDataFetchingThread = fetchDishData()
        dishDataFetchingThread.start()
        dishDataFetchingThread.join()

        init()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init(){
        binding.apply {
            rcView.layoutManager = LinearLayoutManager(view?.context)
            rcView.adapter = adapter
            val db = context?.let { DbHelper(it) }
            for(i in dishes){
                db?.addDish(i)
                adapter.addDish(i)
            }

        }
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
    override fun onLongClick(dish: Dish){
        val db = context?.let { DbHelper(it) }
        val email = arguments?.getString("email")
        val userId = db?.getUserID(email.toString())
        val dishId = db?.getDish(dish.name)
        val builder = AlertDialog.Builder(context)
        builder.setTitle(dish.name)
            .setMessage("Вы хотите сохранить этот рецепт?")
            .setPositiveButton("Сохранить") { dialog, _ ->
                if (userId != null) {
                    if (dishId != null) {
                        db?.saveFavDish(userId.toInt(), dishId)
                    }
                }
                userId?.let { db?.getFavDish(it) }
                dialog.dismiss()
            }.setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss() }.create().show()
    }
}
















