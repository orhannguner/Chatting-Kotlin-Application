package com.example.chattingnew

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FriendRequestsActivity : AppCompatActivity() {

    private lateinit var mDbRef: DatabaseReference
    private lateinit var requestList: ArrayList<String>
    private lateinit var listView: ListView
    private lateinit var adapter: FriendRequestAdapter
    private lateinit var currentUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_requests)

        mDbRef = FirebaseDatabase.getInstance().reference
        currentUid = FirebaseAuth.getInstance().currentUser!!.uid

        listView = findViewById(R.id.listRequests)
        requestList = ArrayList()

        adapter = FriendRequestAdapter(this, requestList) { requesterUid ->
            // ✅ Kabul işlemi
            mDbRef.child("friends").child(currentUid).child(requesterUid).setValue(true)
            mDbRef.child("friends").child(requesterUid).child(currentUid).setValue(true)

            mDbRef.child("friend_requests").child(currentUid).child(requesterUid).removeValue()

            Toast.makeText(this, "Arkadaşlık kabul edildi", Toast.LENGTH_SHORT).show()
        }

        listView.adapter = adapter

        // 🔄 Gelen istekleri yükle
        mDbRef.child("friend_requests").child(currentUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    requestList.clear()
                    for (child in snapshot.children) {
                        val uid = child.key ?: continue
                        requestList.add(uid)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@FriendRequestsActivity, "Hata: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
