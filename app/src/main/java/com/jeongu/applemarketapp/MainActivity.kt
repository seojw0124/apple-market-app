package com.jeongu.applemarketapp

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeongu.applemarketapp.data.ProductManager
import com.jeongu.applemarketapp.databinding.ActivityMainBinding

private const val EXTRA_PRODUCT = "product"

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val productListAdapter by lazy {
        ProductListAdapter(
            ProductManager.getProductList(),
            onClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra(EXTRA_PRODUCT, product)
                startActivity(intent)
            },
            onLongClick = { product ->
                Toast.makeText(this, "Long Clicked! ${product.title}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvProductList.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }
    }
}