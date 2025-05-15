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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var edtMail : EditText
    private lateinit var edtPw : EditText
    private lateinit var btnSignUp: Button
    private lateinit var mDbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mAuth= FirebaseAuth.getInstance()
        edtMail = findViewById(R.id.edtMail)
        edtPw = findViewById(R.id.edtPassword)
        btnSignUp = findViewById(R.id.btnSignUpp)



        btnSignUp.setOnClickListener {
            val email= edtMail.text.toString()
            val pw = edtPw.text.toString()

            signup(email,pw);
        }


    }
    private fun signup(email:String,pw:String){
        mAuth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    addUsertoDatabase(email,pw,mAuth.currentUser?.uid!!)
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                   Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                }
            }


    }
    private fun addUsertoDatabase(email: String,pw: String, uid: String){

        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(email,pw,uid))



    }
}