package com.softradix.singlekoin.koinDiExample.actvities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.softradix.singlekoin.R
import com.softradix.singlekoin.koinDiExample.model.User
import com.softradix.singlekoin.koinDiExample.adapter.MainAdapter
import com.softradix.singlekoin.koinDiExample.viewModel.MainViewModel
import com.softradix.singlekoin.koinDiExample.utils.Status
import com.softradix.singlekoin.koinDiExample.viewModel.ConnectionLiveData
import kotlinx.android.synthetic.main.activity_koin_di.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class KoinDiActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    lateinit var connectionLiveData: ConnectionLiveData

    private lateinit var mainAdapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this)
        setContentView(R.layout.activity_koin_di)


        setupUI()
        setupObserver()

    }

    private fun setupUI() {
        mainAdapter = MainAdapter(arrayListOf())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@KoinDiActivity)
            adapter = mainAdapter
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    private fun setupObserver() {
        connectionLiveData.observe(this) {
            it?.let {
                if (it) {
//                    Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show()
                } else {
//                    Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show()

                }
            }
        }


        mainViewModel.users.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(users: List<User>) {
        users.forEach {
            Log.e("TAG", "renderList: " + it.email)
        }
        mainAdapter.addData(users)
        mainAdapter.notifyDataSetChanged()
    }

}