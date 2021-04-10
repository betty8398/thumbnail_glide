package com.example.mediaplayer2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private RecyclerView.ViewHolder viewHolder;
    private Button bt_save;
    private VideoView mVideoView;
    private String TAG = "TAG";
    private TextView tx_time;
    private View Anchor;
    private int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //imageInternet 網路圖像載入
        ImageView imageView = findViewById(R.id.imageInternet);
//        Glide.with(this)
//                .load("https://goo.gl/gEgYUd")
//                .into(imageView);

        //imageVideo 影片截圖
        ImageView thumbnail1 = (ImageView) findViewById(R.id.imageVideo);
//        Glide.with(this)
//                .load(R.raw.yonikakerumv)
//                .thumbnail(0.1f)
//                .into(thumbnail1);


        //TODO 3 播放器
        //加入元件id
        mVideoView = findViewById(R.id.videoView);

        //給 mVideoView 一個控制器
        MediaController mediaController = new MediaController(this);


        mVideoView.setMediaController(mediaController);


        //設定 mVideoView callback
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaController.setAnchorView(mVideoView);
                mediaController.show();
                mVideoView.seekTo(position);
                if (position == 0) {
                    mp.start();
                } else {
                    mp.pause();
                }
            }
        });

        //設定 mVideoView 要播的檔案
//        Uri uri = Uri.parse("/sdcard/Movies/BDC789456s.mp4");
//        mVideoView.setVideoURI(uri);
        String sourcePath = "/sdcard/Movies/screen-20210407-161618.mp4";
        mVideoView.setVideoPath(sourcePath);
        //設定是否按下按鈕
        //mVideoView.setPressed(true);

        //mediaController.show();
        //TODO 3 END 播放器

        //TODO 影片時間 要修改simpleDateFormat的起始時間
        tx_time = findViewById(R.id.tx_time);
        tx_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                Date date = new Date(mVideoView.getCurrentPosition());
                res = simpleDateFormat.format(date);
                tx_time.setText(res);
            }
        });

        //TODO button_save 儲存影片截圖
        bt_save = findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暫停播放
                mVideoView.pause();
                GetVideoFrame getVideoFrame= new GetVideoFrame(MainActivity.this, sourcePath,mVideoView);
                thumbnail1.setImageBitmap(getVideoFrame.getVideoFrame());
            }
        });


        //ask for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
        } else {
            //do nothing at the moment
        }
    }

    //ask for storage permission
    public void onRequestPermissionoResult(int requestCode ,String[] permission,int[] grantResult){
        if(requestCode==1000){
            if(grantResult[0] == RESULT_OK){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }



    /**
     * 以下是播放器 callback
     */

    @Override
    protected void onResume() {
//        mVideoView.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(this, "播放完畢", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "發生錯誤", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}