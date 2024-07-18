package com.jeongu.applemarketapp.ui

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
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
        initScrollToTopButton()
        initNotificationIconImage()
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

    private fun initScrollToTopButton() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_scroll_button)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_scroll_button)
        var isTop = true

        with(binding) {
            rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!rvProductList.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        ivScrollToTop.apply {
                            visibility = View.GONE
                            startAnimation(fadeOut)
                        }
                        isTop = true
                    } else {
                        if (isTop) {
                            ivScrollToTop.apply {
                                visibility = View.VISIBLE
                                ivScrollToTop.startAnimation(fadeIn)
                            }
                            isTop = false
                        }
                    }
                }
            })

            ivScrollToTop.setOnClickListener {
                binding.rvProductList.smoothScrollToPosition(0)
            }
        }
    }

    private fun initNotificationIconImage() {
        binding.toolbar.ivNotificationIcon.setOnClickListener {
            requestPermission()
            createNotification()
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                // 알림 권한이 없다면, 사용자에게 권한 요청
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
        }
    }

    private fun createNotification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = getNotificationChannel()
        manager.createNotificationChannel(channel)

        val pendingIntent = createPendingIntent()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID).run {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setWhen(System.currentTimeMillis())
            setContentTitle(CHANNEL_ID)
            setContentText(CHANNEL_NAME)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setContentIntent(pendingIntent)
        }.build()

        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getNotificationChannel(): NotificationChannel {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        return NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = "키워드 알림 설명"
            setShowBadge(true)
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            setSound(uri, audioAttributes)
            enableVibration(true)
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
        const val CHANNEL_ID = "키워드 알림"
        const val CHANNEL_NAME = "설정한 키워드에 대한 알림 도착했습니다!!"
        const val NOTIFICATION_ID = 1
    }
}