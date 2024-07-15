package com.jeongu.applemarketapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.jeongu.applemarketapp.data.ProductInfo
import com.jeongu.applemarketapp.data.ProductManager
import com.jeongu.applemarketapp.databinding.ActivityMainBinding

private const val EXTRA_PRODUCT = "product"

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val productListAdapter by lazy {
        ProductListAdapter(
            onClick = { product ->
                navigateToProductDetail(product)
            },
            onLongClick = { product ->
                showDeleteProductDialog(product)
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
        setLayout()
    }

    private fun setLayout() {
        initRecyclerView()
        setOnBackPressedHandler()
    }

    private fun initRecyclerView() {
        binding.rvProductList.apply {
            adapter = productListAdapter
            productListAdapter.submitList(ProductManager.getList().toList())
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setOnBackPressedHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })
    }

    private fun navigateToProductDetail(product: ProductInfo) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra(EXTRA_PRODUCT, product)
        startActivity(intent)
    }

    private fun showDeleteProductDialog(product: ProductInfo) {
        showDialog(
            getString(R.string.dialog_title_delete_product),
            getString(R.string.dialog_message_delete_product),
        ) {
            val isDeleted = ProductManager.removeItem(product)
            if (isDeleted) {
                productListAdapter.submitList(ProductManager.getList().toList())
            }
        }
    }

    private fun showExitDialog() {
        showDialog(
            getString(R.string.dialog_title_finish_app),
            getString(R.string.dialog_message_finish_app),
        ) {
            finish()
        }
    }

    private fun showDialog(
        title: String,
        message: String,
        action: () -> Unit
    ) {
        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> action()
                DialogInterface.BUTTON_NEGATIVE -> return@OnClickListener
            }
        }

        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setIcon(R.drawable.ic_comment)
        }.apply {
            setPositiveButton(getString(R.string.dialog_label_positive), listener)
            setNegativeButton(getString(R.string.dialog_label_negative), listener)
        }.show()
    }
}