package bloomand.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)


        findViewById<Button>(R.id.LogButton).setOnClickListener{
            val acc_email = findViewById<EditText>(R.id.LogEmail).text.toString().trim()
            val acc_password = findViewById<EditText>(R.id.LogPassword).text.toString().trim()

            if(acc_email == "" || acc_password == ""){
                Toast.makeText(this,"Empty field",Toast.LENGTH_SHORT).show()
            }else {
                val db = DbHelper(this)
                val isAlready = db.checkUser(acc_email)
                if(isAlready){
                    val isAuth = db.getUser(acc_email, acc_password)
                    if(isAuth){
                        Toast.makeText(this,"Yeah!",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MenuActivity::class.java)
                        intent.putExtra("user_email", acc_email)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Wrong password",Toast.LENGTH_SHORT).show()
                    }

                    findViewById<EditText>(R.id.LogEmail).text.clear()
                    findViewById<EditText>(R.id.LogPassword).text.clear()
                }else{
                    Toast.makeText(this,"No Account",Toast.LENGTH_SHORT).show()
                }

            }

        }

        findViewById<TextView>(R.id.link_register).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}