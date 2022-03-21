package kr.dada.torigin

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kr.dada.torigin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setLayout()
    }

    override fun onResume() {
        super.onResume()

        if(binding != null) {
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
                if(link.isNotBlank()) {
                    val origin = link.split("&fname=")[1]

                    if(origin.isNotBlank()) {
                        val url = origin.replace("%3A", ":")
                            .replace("%2F", "/")

//                        Toast.makeText(this@MainActivity, "url : $url", Toast.LENGTH_LONG).show()

                        val intent = Intent(ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "올바른 링크를 입력해주세요!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "링크를 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}