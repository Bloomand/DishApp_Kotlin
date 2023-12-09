package bloomand.myapplication.ui.current

import android.view.View
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import bloomand.myapplication.DbHelper
import bloomand.myapplication.R
import bloomand.myapplication.databinding.FragmentCurrentBinding

class CurrentFragment : Fragment(){

    private var _binding: FragmentCurrentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val imageIdList = listOf(
        R.drawable.dish1,
        R.drawable.dish2,
        R.drawable.dish3,
        R.drawable.dish4)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val id = arguments?.getInt("id")
        val name = arguments?.getString("name")
        val calorie = arguments?.getInt("calorie")
        val time = arguments?.getInt("time")
        val difficulty = arguments?.getInt("difficulty")
        val ingredient = arguments?.getString("ingredient")
        val image = arguments?.getInt("imageID")

        val email = arguments?.getString("email")

        val db = context?.let { DbHelper(it) }
        db?.getUserID(email.toString())?.let {
            if (id != null) {
                db.saveFavDish(it,id)
            }
        }

        val im = root.findViewById<ImageView>(R.id.image_dish)
        im.setImageResource(imageIdList[image!!.toInt()])

        val textName: TextView = root.findViewById(R.id.text_current)
        textName.text = name

        val textTime: TextView = root.findViewById(R.id.time)
        textTime.text = time.toString() + " minutes"
        val textCalorie: TextView = root.findViewById(R.id.calorie)
        textCalorie.text = calorie.toString() + " cal"
        val textIngredient: TextView = root.findViewById(R.id.ingredient)
        textIngredient.text = ingredient
        val textDifficulty: TextView = root.findViewById(R.id.difficulty)
        textDifficulty.text = difficulty.toString() + " difficulty"


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

