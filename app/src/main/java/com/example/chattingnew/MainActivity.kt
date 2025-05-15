package com.example.chattingnew

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter : UserAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference
    private lateinit var btnArk : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        btnArk = findViewById(R.id.btnBul)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList=ArrayList()
        adapter=UserAdapter(this,userList)
        userRecyclerView = findViewById(R.id.userRecycler)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter=adapter

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecycler)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        val currentUid = mAuth.currentUser!!.uid
        btnArk.setOnClickListener {
            intent = Intent(this@MainActivity, Friendship::class.java)
            startActivity(intent)
            finish()
        }
// 🔥 Önce arkadaş UID'lerini al
        mDbRef.child("friends").child(currentUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(friendsSnapshot: DataSnapshot) {
                    val friendUids = mutableSetOf<String>()
                    for (child in friendsSnapshot.children) {
                        child.key?.let { friendUids.add(it) }
                    }

                    // 🔄 Tüm kullanıcıları dinle ama sadece arkadaş olanları listele
                    mDbRef.child("user")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                userList.clear()
                                for (userSnap in userSnapshot.children) {
                                    val user = userSnap.getValue(User::class.java)
                                    val uid = userSnap.key
                                    if (uid != currentUid && friendUids.contains(uid)) {
                                        user?.let { userList.add(it) }
                                    }
                                }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            mAuth.signOut()
            val intent = Intent(this@MainActivity,Login::class.java)
            finish()
            startActivity(intent)

            return true
        }
        return true
    }
}