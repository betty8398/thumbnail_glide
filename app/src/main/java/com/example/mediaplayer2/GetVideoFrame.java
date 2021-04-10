package com.example.mediaplayer2;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.widget.VideoView;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class GetVideoFrame {
    Context context;
    String sourcePath;
    VideoView video;

    public GetVideoFrame(Context context, String sourcePath, VideoView video) {
        this.context = context;
        this.sourcePath = sourcePath;
        this.video = video;
    }

    public Bitmap getVideoFrame() {

        Bitmap bmp = null;
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();

        try {
            //指定影片
//            retriever.setDataSource(context, uri);
            retriever.setDataSource(sourcePath);

            //
            String timeString = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
            long titalTime = Long.parseLong(timeString) * 1000;
            int duration =video.getDuration();
            long videoPosition = titalTime *video.getCurrentPosition() / duration;
            if (videoPosition > 0) {
                bmp = retriever.getFrameAtTime(videoPosition, MediaMetadataRetriever.OPTION_CLOSEST);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.fillInStackTrace();
            }
        }
        return bmp;
    }
}
