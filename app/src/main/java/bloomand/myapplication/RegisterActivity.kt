package bloomand.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        findViewById<Button>(R.id.LogButton).setOnClickListener{
            val acc_email = findViewById<EditText>(R.id.LogEmail).text.toString().trim()
            val acc_password = findViewById<EditText>(R.id.LogPassword).text.toString().trim()

            if(acc_email == "" || acc_password == ""){
                Toast.makeText(this,"Empty field",Toast.LENGTH_SHORT).show()
            }else {
                val account = User(acc_email, acc_password)
                val db = DbHelper(this)
                val isAlready = db.checkUser(acc_email)
                if(isAlready){
                    Toast.makeText(this,"Already have an account. Sign in",Toast.LENGTH_SHORT).show()
                }else{
                    db.addUser(account)
                    Toast.makeText(this, "Account was created", Toast.LENGTH_SHORT).show()

                    findViewById<EditText>(R.id.LogEmail).text.clear()
                    findViewById<EditText>(R.id.LogPassword).text.clear()
                }

            }

        }

        findViewById<TextView>(R.id.link_auth).setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
    }
}