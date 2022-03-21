package kr.dada.torigin

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
        }
    }

    private fun setLayout() {
        /**
         * https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbLKQBO%2FbtrwqL6sSZ4%2FKXGCHigx7EieaXC1kM8XN1%2Fimg.jpg
         */
        binding.apply {
            activityMainTvGo.setOnClickListener {
                val link = activityMainEtLink.text.toString()
                if (link.isNotBlank()) {
                    val origin = link.split("&fname=")[1]

                    if (origin.isNotBlank()) {
                        val url = origin.replace("%3A", ":")
                            .replace("%2F", "/")

//                        val intent = Intent(ACTION_VIEW, Uri.parse(url))
//                        startActivity(intent)
                        downloadImg(url)
                    } else {
                        Toast.makeText(this@MainActivity, "올바른 링크를 입력해주세요!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "링크를 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun downloadImg(url: String) {
        val fileName = System.currentTimeMillis().toString()
        val outputFilePath: String =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/$fileName.png"

        val file = File(outputFilePath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
//        val file = File(getExternalFilesDir(null), "$fileName.png")

        val downloadmanager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setTitle("$fileName.png")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            setVisibleInDownloadsUi(false)
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
            setDestinationUri(Uri.fromFile(file))
        }
        downloadmanager!!.enqueue(request)
    }
}