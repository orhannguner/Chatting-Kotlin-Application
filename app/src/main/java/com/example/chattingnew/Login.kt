package com.example.chattingnew

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var edtMail : EditText
    private lateinit var edtPw : EditText
    private lateinit var btnLogin : Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth=FirebaseAuth.getInstance()
        edtMail = findViewById(R.id.edtEmail)
        edtPw = findViewById(R.id.edtPw)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            intent = Intent(this@Login,SignUp::class.java)
            finish()
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtMail.text.toString()
            val pw = edtPw.text.toString()
            login(email,pw);

        }

    }
    private fun login(email:String,pw:String){

        mAuth.signInWithEmailAndPassword(email, pw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val intent = Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                   Toast.makeText(this@Login,"Kullanıcı giremedi",Toast.LENGTH_SHORT).show()
                }
            }
    }


}