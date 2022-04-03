package com.example.androidlab1

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab1.databinding.FragmentGameBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var view: RecyclerView
    lateinit var adapter: Adapter
    private val reference by lazy { DB.getReference() }
    private val user by lazy { User() }
    lateinit var hash: String
    var size = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameBinding.inflate(layoutInflater)

        binding.tSatietyCount.text = satiety.toString()

        hash = arguments?.getString("hash").toString()
        getUser(hash)

        view = RecyclerView(binding.root.context)

        val builderRecords = AlertDialog.Builder(context)
        builderRecords.setCancelable(true)
        builderRecords.setTitle("Рекорды")
        builderRecords.setPositiveButton("Ok") { recordDialog, _ ->
            recordDialog.dismiss()
        }
        builderRecords.setView(view)
        val recordDialog = builderRecords.create()

        binding.achivements.setOnClickListener{
            var max = 0
            user.list.forEach { if (it.points>max)
                max = it.points }
            var aim = max+1
            aim = max.div(25)*25+25

            val builder = AlertDialog.Builder(context)
            builder
                .setTitle("Результат ${max.div(25)}:")
                .setIcon(R.drawable.star)
                .setItems(arrayOf("Текущий максимальный результат: $max ","Следующая цель: $aim"),null)
                .setPositiveButton("OK", null)
                .create()
                .show()

        }

        binding.records.setOnClickListener {
            adapter.addToList(user.list)
            view.layoutManager = LinearLayoutManager(requireContext())
            recordDialog.show()

        }

        binding.infoImage.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setView(R.layout.fragment_info)
                .setPositiveButton("OK", null)
                .create()
                .show()
        }

        binding.shareImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "My record in game Feed the cat is $satiety")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        binding.bFeed.setOnClickListener {
            satiety++

            reference.child(user.login.hashCode().toString()).child("list").child(size.toString())
                .setValue(
                    Pair(
                        satiety,
                        SimpleDateFormat(DATE_FORMAT).format(Date().time).toString()
                    )
                )
            user.list[user.list.size - 1] =
                Pair(satiety, SimpleDateFormat(DATE_FORMAT).format(Date().time).toString())

            binding.tSatietyCount.setText(satiety.toString())
            if (satiety % 15 == 0) {
                binding.iCat.startAnimation(AnimationUtils.loadAnimation(context, R.anim.animation))
            }

        }
        adapter = Adapter()
        view.adapter = adapter
        return binding.root
    }

    private fun getUser(hash: String) {
        reference.child(hash).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.getValue(User::class.java)!!
                user.list.clear()
                user.list.addAll(temp.list)
                user.login = temp.login
                size = temp.list.size
                reference.child(user.login.hashCode().toString()).child("list")
                    .child(size.toString())

                user.list.add(Pair())
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    }

    companion object {
        const val DATE_FORMAT = "dd MM yyyy, HH:mm:ss"
        var satiety = 0
    }
}