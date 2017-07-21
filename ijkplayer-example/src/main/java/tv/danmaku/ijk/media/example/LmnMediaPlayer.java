package tv.danmaku.ijk.media.example;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import tv.danmaku.ijk.media.example.application.Settings;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import java.io.IOException;

/**
 * 简单的基本播放器功能，只提供接口
 * Created by GuoShaoHong on 2017/7/21.
 */

public class LmnMediaPlayer extends FrameLayout {
    //the real play i need to playing
    private IMediaPlayer mMediaPlayer = null;
    private Settings mSettings;
    private String mPath = "";
    SurfaceView surfaceView;

    public LmnMediaPlayer(Context context) {
        super(context);
        initVideoView(context);
    }

    public LmnMediaPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView(context);
    }

    public LmnMediaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView(context);
    }

    private void initVideoView(Context context) {
        //获取焦点，不知道有没有必要~。~
        setFocusable(true);

        mSettings = new Settings(context);

        //生成一个新的surface view
        surfaceView = new SurfaceView(context);
        surfaceView.getHolder().addCallback(new LmnSurfaceCallback());
        FrameLayout.LayoutParams layoutParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        surfaceView.setLayoutParams(layoutParams);
        this.addView(surfaceView);
    }

    private class LmnSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            createPlayer();

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mMediaPlayer != null) {
//                mMediaPlayer.start();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mMediaPlayer.setDisplay(null);
        }
    }

    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        mPath = path;
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void createPlayer() {

        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        if (mSettings.getUsingMediaCodec()) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            if (mSettings.getUsingMediaCodecAutoRotate()) {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
            } else {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
            }
            if (mSettings.getMediaCodecHandleResolutionChange()) {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
            } else {
                ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
            }
        } else {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        }

        if (mSettings.getUsingOpenSLES()) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        } else {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
        }

        String pixelFormat = mSettings.getPixelFormat();
        if (TextUtils.isEmpty(pixelFormat)) {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        } else {
            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat);
        }
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

        mMediaPlayer = ijkMediaPlayer;


        mMediaPlayer.setOnPreparedListener(mPreparedListener);

        try {
            mMediaPlayer.setDataSource(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //给mediaPlayer设置视图
        mMediaPlayer.setDisplay(surfaceView.getHolder());

        mMediaPlayer.prepareAsync();
    }

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            mMediaPlayer.seekTo(1000);
            ((IjkMediaPlayer) mMediaPlayer).setSpeed(1);

            start();
        }
    };
}
