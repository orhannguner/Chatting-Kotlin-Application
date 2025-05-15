package com.example.chattingnew

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Friendship : AppCompatActivity() {

    private lateinit var btnAraa : Button
    private lateinit var txtAra: EditText
    private lateinit var mDbRef : DatabaseReference
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mailText : TextView
    private lateinit var ekleImg : ImageView
    private lateinit var btnList : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendship)
        btnAraa = findViewById(R.id.btnAra)
        txtAra = findViewById(R.id.edtAra)
        mDbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        mailText = findViewById(R.id.txtMail)
        ekleImg = findViewById(R.id.ekleresim)
        btnList = findViewById(R.id.btnListele)

        btnList.setOnClickListener {

            intent = Intent(this@Friendship,FriendRequestsActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnAraa.setOnClickListener {

            val mail = txtAra.text.toString()
            var bulundu = false
            var foundUserUid = ""
            mDbRef.child("user").addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    for(postSnapShot in snapshot.children){
                        val currentUser = postSnapShot.getValue(User::class.java)
                        if(mail==currentUser?.mail){
                            bulundu = true
                            foundUserUid = postSnapShot.key.toString()
                            mailText.text = currentUser.mail

                            ekleImg.setOnClickListener {
                                val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
                                mDbRef.child("friend_requests").child(foundUserUid).child(currentUid).setValue("pending")
                                Toast.makeText(this@Friendship, "İstek gönderildi", Toast.LENGTH_SHORT).show()
                            }
                            break
                        }
                        else{
                            bulundu= false
                        }
                    }
                    if(!bulundu){
                        Toast.makeText(this@Friendship, "Kullanıcı Bulunamadı",Toast.LENGTH_SHORT).show()}


                }



                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



        }



    }
}