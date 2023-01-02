package kr.dada.torigin

import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kr.dada.torigin.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setLayout()
    }

    override fun onResume() {
        super.onResume()

        if (binding != null) {
            binding.activityMainEtLink.text = null
            binding.showPreview = false
        }
    }

    private fun getOriginLink(): String {
        val link = binding.activityMainEtLink.text.toString()
        var url = ""
        if (link.isNotBlank()) {
            val origin = link.split("&fname=")[1]

            if (origin.isNotBlank()) {
                url = origin.replace("%3A", ":").replace("%2F", "/")
            } else {
                Toast.makeText(this@MainActivity, "올바른 링크를 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@MainActivity, "링크를 입력해주세요!", Toast.LENGTH_SHORT).show()
        }
        return url
    }

    private fun setLayout() {
        /**
         * https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbLKQBO%2FbtrwqL6sSZ4%2FKXGCHigx7EieaXC1kM8XN1%2Fimg.jpg
         */
        binding.apply {
            activityMainTvPaste.throttleClick {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (!(clipboard.hasPrimaryClip())) {

                } else if ((clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)) == false) {

                } else {
                    val item: ClipData.Item? = clipboard.primaryClip?.getItemAt(0)
                    binding.showPreview = false
                    binding.activityMainEtLink.setText(item?.text)
                }
            }

            activityMainTvParse.throttleClick {
                val link = getOriginLink()
                binding.showPreview = link.isNotBlank()
                GlideApp.with(it)
                    .load(getOriginLink())
                    .into(binding.activityMainIvPreview)
            }

            activityMainTvClear.throttleClick {
                binding.activityMainEtLink.text = null
                binding.showPreview = false
            }

            activityMainTvGo.throttleClick {
                downloadImg(getOriginLink())
            }
        }
    }

    private fun downloadImg(url: String) {
        if (url.isBlank()) {
            Toast.makeText(this, "링크를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            binding.showPreview = false
            return
        }
        val fileName = System.currentTimeMillis().toString()
        val outputFilePath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$fileName.png"

        val file = File(outputFilePath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
//        val file = File(getExternalFilesDir(null), "$fileName.png")

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("$fileName.png")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            setVisibleInDownloadsUi(false)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
            setDestinationUri(Uri.fromFile(file))
        }
        downloadManager!!.enqueue(request)
    }
}

fun View.throttleClick(interval: Long? = null, action: (View) -> Unit) {
    setOnClickListener(OnThrottleClickListener(interval ?: 100, action))
}