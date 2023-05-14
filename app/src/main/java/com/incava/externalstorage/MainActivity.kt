package com.incava.externalstorage

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL
import com.incava.externalstorage.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        startLockTask()

    }

    private val binding by lazy {  ActivityMainBinding.inflate(layoutInflater)}
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun initSetPlayer(player: Player) {
        //저장한 파일 경로 외부저장소/BeatPhobia.main01.mp4
        val saveFolderPath = File(
            Environment.getExternalStorageDirectory().absolutePath + "/BeatPhobia",
            "main01.mp4"
        )
        val mediaItem = MediaItem.fromUri(Uri.fromFile(saveFolderPath))
        player.setMediaItem(mediaItem)
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.prepare()
        player.play()
        //Log.i("screenSizeX", screenXSize().toString())
        //Log.i("screenSizeY", screenYSize().toString())
        Log.i("player", player.videoSize.width.toString())
        Log.i("player", player.videoSize.height.toString())
    }

    private fun screenXSize() =
        applicationContext?.resources?.displayMetrics?.widthPixels //화면 사이즈 구하기.

    private fun screenYSize() =
        applicationContext?.resources?.displayMetrics?.heightPixels //화면 사이즈 구하기.

    @RequiresApi(Build.VERSION_CODES.R)
    fun setPlayer() {
        //윈도우 화면 제한 없애기
        // 제생 플레이어
        val player = ExoPlayer.Builder(this).build()
        val player2 = ExoPlayer.Builder(this).build()
        val player3 = ExoPlayer.Builder(this).build()
        Log.i("layoutsize", screenXSize().toString())
        Log.i("layoutsize", screenYSize().toString())
        // screenSize에 맞게 설정
        binding.flPlayerView.layoutParams.width = screenXSize()?.minus(107) ?: 0
        binding.flPlayerView.layoutParams.height = screenYSize() ?: 0
        binding.playerView.resizeMode = RESIZE_MODE_FILL
        binding.playerView.player = player

        initSetPlayer(player)

        //frame1Binding.playerView.player = player
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when (it) {
                true -> {
                    Toast.makeText(this, "권한 허가", Toast.LENGTH_SHORT).show()
                    setPlayer()
                }
                false -> {
                    Toast.makeText(this, "미디어 읽기 권한을 허가해주세요.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

}