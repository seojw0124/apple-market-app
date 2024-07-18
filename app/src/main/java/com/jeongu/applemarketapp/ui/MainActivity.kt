package com.jeongu.applemarketapp.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.jeongu.applemarketapp.R
import com.jeongu.applemarketapp.data.ProductInfo
import com.jeongu.applemarketapp.data.ProductManager
import com.jeongu.applemarketapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProductItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val productListAdapter by lazy {
        ProductListAdapter(
            onClick = { product ->
                navigateToProductDetail(product)
            },
            onLongClick = { product ->
                showDeleteProductDialog(product)
            },
            listener = this
        )
    }
    private lateinit var likeResult: ActivityResultLauncher<Intent>

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
        setLikeResult()
        setOnBackPressedHandler()
    }

    private fun initRecyclerView() {
        binding.rvProductList.apply {
            adapter = productListAdapter
            productListAdapter.submitList(ProductManager.getList().toList())
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setLikeResult() {
        likeResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val isLikeUpdated = result.data?.getBooleanExtra(EXTRA_BOOLEAN_LIKE_UPDATE, false)
                if (isLikeUpdated == true) {
                    productListAdapter.submitList(ProductManager.getList().toList())
                }
            }
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
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, product)
        likeResult.launch(intent)
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
        positiveAction: () -> Unit
    ) {
        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> positiveAction()
                DialogInterface.BUTTON_NEGATIVE -> {}
            }
        }

        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setIcon(R.drawable.ic_comment)
            setPositiveButton(getString(R.string.dialog_label_positive), listener)
            setNegativeButton(getString(R.string.dialog_label_negative), listener)
        }.show()
    }

    override fun onLikeClick(productId: Int, isLikeImageChecked: Boolean) {
        val isLikeUpdated = ProductManager.updateLike(productId, isLikeImageChecked)
        showSnackBar(isLikeUpdated, isLikeImageChecked)
    }

    private fun showSnackBar(isUpdated: Boolean, isLikeImageChecked: Boolean) {
        val message = when {
            isUpdated && isLikeImageChecked -> getString(R.string.message_add_interest_list)
            isUpdated && !isLikeImageChecked -> getString(R.string.message_remove_interest_list)
            else -> getString(R.string.message_unknown_error)
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_BOOLEAN_LIKE_UPDATE = "isLikeUpdated"
    }
}