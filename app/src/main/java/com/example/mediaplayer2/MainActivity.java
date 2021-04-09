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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;


public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private RecyclerView.ViewHolder viewHolder;
    private Button bt_save;
    private VideoView mVideoView;
    private String TAG="TAG";
    private TextView tx_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //imageVideo 影片截圖
        ImageView thumbnail1= (ImageView) findViewById(R.id.imageVideo);
        Glide.with(this)
                .load(R.raw.yonikakerumv)
                .thumbnail(0.1f)
                .into(thumbnail1);


        //imageInternet 網路圖像載入
        ImageView imageView = findViewById(R.id.imageInternet);
        Glide.with(this)
                .load("https://goo.gl/gEgYUd")
                .into(imageView);



        //TODO 3 播放器
        //加入元件id
        mVideoView = findViewById(R.id.videoView);
        //給 mVideoView 一個控制器
        MediaController mediaController = new MediaController(this);
        mVideoView.setMediaController(mediaController);
        //設定 mVideoView callback
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        //設定 mVideoView 要播的檔案
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.yonikakerumv);
        mVideoView.setVideoURI(uri);

        //測試 mediaController
        Log.d(TAG, "onCreate: " + mediaController.isShowing());
        mediaController.show();
        //TODO 3 END 播放器

        //TODO 影片時間 要修改simpleDateFormat的起始時間
        tx_time = findViewById(R.id.tx_time);
        tx_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                Date date = new Date(mVideoView.getCurrentPosition() );
                res = simpleDateFormat.format(date);
                tx_time.setText(res);
            }
        });

        //TODO button 儲存畫面截圖
//        bt_save = findViewById(R.id.bt_save);
//        bt_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//                Bitmap bitmap = getScreenShot(rootView);
//                store(bitmap,"ScreenShot.png");
//            }
//        });


        //TODO 擷取影音某個秒數的截圖
       // getVideoThumnail("R.raw.yonikakerumv",1000*1000);//這是視訊截圖
        loadVideoScreenshot(this,"R.raw.yonikakerumv",thumbnail1,0);



        //ask for permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
        }else {
            //do nothing at the moment
        }
    }

    //ask for storage permission

    //get screen shot of the app
//    public static Bitmap getScreenShot (View view){
//        View screenView = view.getRootView();
//        screenView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
//        screenView.setDrawingCacheEnabled(false);
//        return bitmap;
//    }
//
//    //store the image on the device
//    public void store(Bitmap bitmap, String fileName){
//        String dirPath = Environment.getDownloadCacheDirectory().getAbsolutePath()+"/MyFiles";
//        File dir = new File(dirPath);
//        if(!dir.exists()){
//            dir.mkdirs();
//        }
//        File file = new File(dirPath,fileName);
//
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void onRequestPermissionoResult(int requestCode ,String[] permission,int[] grantResult){
//        if(requestCode==1000){
//            if(grantResult[0] == RESULT_OK){
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /**
     * 顯示視訊 第三秒 那一幀
     *
     * @param context
     * @param uri
     * @param imageView
     * @param frameTimeMicros 要擷取得時間。單位：微秒
     */
    public static void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {

        // 這裡的時間是以微秒為單位
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update(("com.example.mediaplayer2" + "RotateTransform").getBytes("utf-8"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

    /**
     * 獲得視訊某一幀的縮圖
     *
     * @param videoPath 視訊地址
     * @param timeUs 微秒，注意這裡是微秒 1秒 = 1 * 1000 * 1000 微秒
     *
     * @return 擷取的圖片
     */
    public static Bitmap getVideoThumnail(String videoPath, long timeUs) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        // 獲取第一個關鍵幀
        // OPTION_CLOSEST 在給定的時間，檢索最近一個幀，這個幀不一定是關鍵幀。
        // OPTION_CLOSEST_SYNC 在給定的時間，檢索最近一個關鍵幀。
        // OPTION_NEXT_SYNC 在給定時間之後，檢索一個關鍵幀。
        // OPTION_PREVIOUS_SYNC 在給定時間之前，檢索一個關鍵幀。
        return media.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_NEXT_SYNC);

        // 得到視訊第一幀的縮圖
         //return media.getFrameAtTime();
    }

    /**
     * 以下是播放器 callback
     */

    @Override
    protected void onResume() {
        mVideoView.start();
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