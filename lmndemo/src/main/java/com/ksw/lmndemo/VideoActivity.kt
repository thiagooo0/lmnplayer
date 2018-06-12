package com.ksw.lmndemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ksw.lmnview.LmnPlayer
import com.ksw.lmnview.OnLmnReadyCallBack

import kotlinx.android.synthetic.main.content_video.*

class VideoActivity : AppCompatActivity() {
    companion object {
        fun actionActivity(context: Context, path: String) {
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra("path", path)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        val path = intent.getStringExtra("path") ?: return
        lmn_view.getLmnViewAsync(object : OnLmnReadyCallBack {
            override fun onLmnReady(lmnPlayer: LmnPlayer) {
                lmnPlayer.load(path)
            }

            override fun onLmnFail(error: String) {
                Toast.makeText(this@VideoActivity, "创建播放器失败: $error", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}
