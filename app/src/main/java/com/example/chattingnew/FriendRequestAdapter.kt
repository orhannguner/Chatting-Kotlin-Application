package com.example.chattingnew

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*

class FriendRequestAdapter(
    private val context: Context,
    private val requestList: ArrayList<String>,
    private val onAcceptClick: (String) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = requestList.size

    override fun getItem(position: Int): Any = requestList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_friend_request, parent, false)

        val txtMail = view.findViewById<TextView>(R.id.txtRequestMail)
        val btnAccept = view.findViewById<ImageButton>(R.id.btnAccept)

        val requesterUid = requestList[position]

        // UID'den e-posta bilgisi alalım
        val mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(requesterUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    txtMail.text = user?.mail ?: "Bilinmeyen"
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        btnAccept.setOnClickListener {
            onAcceptClick(requesterUid)
        }

        return view
    }
}
