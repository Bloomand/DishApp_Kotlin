package bloomand.myapplication

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import bloomand.myapplication.ui.current.CurrentFragment
import bloomand.myapplication.ui.liked.LikedFragment
import bloomand.myapplication.ui.search.SearchFragment

class MenuActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val email = intent.getStringExtra("user_email")

        loadFragment(SearchFragment(),email.toString())
        bottomNav = findViewById(R.id.nav_view) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_search -> {
                    loadFragment(SearchFragment(),email.toString())
                    true
                }
                R.id.navigation_current -> {
                    loadFragment(CurrentFragment(),email.toString())
                    true
                }
                R.id.navigation_liked -> {
                    loadFragment(LikedFragment(),email.toString())
                    true
                }
                else -> { true }
            }
        }
    }
    private fun loadFragment(fragment: Fragment, email: String){
        val transaction = supportFragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putString("email", email)
        fragment.arguments = bundle

        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}