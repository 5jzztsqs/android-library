package com.android.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;


@SuppressLint("NewApi")
public class VideoCaptureActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = VideoCaptureActivity.class.getSimpleName();

    private static final String PERMISSIONS[] = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String KEY_RECORD_PARAM = "key_record_param";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private CameraDevice mCameraDevice;
    private CameraCaptureSession mPreviewSession;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                              int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };

    private Size mPreviewSize;
    private Size mVideoSize;
    private CaptureRequest.Builder mPreviewBuilder;
    private MediaRecorder mMediaRecorder;
    private boolean mIsRecordingVideo;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private String mCameraId;
    private CameraManager mCameraManager;
    private AutoFitTextureView mTextureView;
    private Chronometer mChronometer;
    private ClockView mClockView;
    private ImageView mRotate;
    private RecordParam recordParam;
    private RelativeLayout mVideoViewContainer;
    private VideoView mVideoView;
    private TextView mBack;
    private TextView mSubmit;
    private File mOutputFile;
    private int clockSeconds = 0;


    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != mTextureView) {
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            VideoCaptureActivity.this.finish();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_capture);
        mClockView = findViewById(R.id.clockView);
        mChronometer = findViewById(R.id.chronometer);
        mTextureView = findViewById(R.id.textureView);
        mRotate = findViewById(R.id.rotate);
        mVideoViewContainer = findViewById(R.id.videoViewContainer);
        mBack = findViewById(R.id.back);
        mSubmit = findViewById(R.id.submit);
        mVideoView = findViewById(R.id.videoView);

        mRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCamera();
                if (String.valueOf(CameraCharacteristics.LENS_FACING_FRONT).equals(mCameraId)) {
                    mCameraId = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);
                } else {
                    mCameraId = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
                }
                openCameraById(mTextureView.getWidth(), mTextureView.getHeight(), mCameraId);
            }
        });
        this.setupVideoView();

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra(KEY_RECORD_PARAM);
        if (jsonString != null) {
            recordParam = JSON.parseObject(jsonString, RecordParam.class);
            int duration = recordParam.getMaxDuration();
            if (duration > RecordParam.DEFAULT_MAX_DURATION) {
                duration = RecordParam.DEFAULT_MAX_DURATION;
            }
            if (duration < RecordParam.DEFAULT_MIN_DURATION) {
                duration = RecordParam.DEFAULT_MIN_DURATION;
            }
            final int maxDuration = duration;
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = new Date(clockSeconds * 1000);
                    mChronometer.setText(sdf.format(date));
                    clockSeconds++;
                }
            });
            mClockView.setMaxClock(duration);
            mClockView.setOnClockListener(new ClockView.OnClockListener() {
                @Override
                public void onStartClock() {
                    clockSeconds = 0;
                    if (mVideoViewContainer.getVisibility() == View.GONE) {
                        recordParam.setVideoPath(null);
                        recordParam.setDuration(0);
                        mChronometer.setBase(SystemClock.elapsedRealtime() + maxDuration * 1000);
                        startRecordingVideo();
                    }
                }

                @Override
                public void onFinishClock(int seconds) {
                    if (mIsRecordingVideo) {
                        mRotate.setVisibility(View.GONE);
                        mChronometer.setVisibility(View.GONE);
                        mClockView.setVisibility(View.GONE);
                        mClockView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRotate.setVisibility(View.VISIBLE);
                                mClockView.setVisibility(View.VISIBLE);
                                stopRecordingVideo();
                                recordParam.setVideoPath(mOutputFile.getAbsolutePath());
                                recordParam.setDuration(seconds);
                            }
                        }, 1000);

                    }
                }

            });
        }
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            beginPreview();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("是否允许访问您设备上的照片、媒体内容和文件的权限？")
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EasyPermissions.requestPermissions(
                                    new PermissionRequest.Builder(VideoCaptureActivity.this, 100, PERMISSIONS)
                                            .build());
                        }
                    })
                    .create().show();

        }
    }

    private void setupVideoView() {
        /*MediaController mediaController = new MediaController(this) {
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    finish();
                    return true;
                }
                return super.dispatchKeyEvent(event);
            }
        };
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);*/
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.seekTo(0);
                if (mVideoViewContainer.getVisibility() == View.VISIBLE) {
                    playVideo();
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        beginPreview();
    }


    private void beginPreview() {
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        toast("请进设置开启相应的权限");
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    /*配置录制参数*/
    private void setUpMediaRecorder() throws IOException {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mOutputFile = new File(Environment.getExternalStorageDirectory(), "video.mp4");
        if (mOutputFile.exists()) {
            mOutputFile.delete();
        }
        mMediaRecorder.setOutputFile(mOutputFile.getAbsolutePath());
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT).equals(mCameraId) ? 0 : 2;
        int orientation = ORIENTATIONS.get(rotation);
        mMediaRecorder.setOrientationHint(orientation);
        mMediaRecorder.prepare();
    }

    /*打开相机*/
    private void openCamera(int width, int height) {
        if (isFinishing()) {
            return;
        }

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(3000, TimeUnit.MILLISECONDS)) {
                toast("请赋予设备上的照片、媒体内容和文件的访问权限，然后重试");
                finish();
                return;
                // throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            mCameraId = mCameraManager.getCameraIdList()[0];
            mRotate.setVisibility(View.VISIBLE);
            openCameraById(width, height, mCameraId);
        } catch (CameraAccessException e) {
            toast("无法访问摄像头");
            finish();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            new ErrorDialog().show(getFragmentManager(), "dialog");
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private void openCameraById(int width, int height, String mCameraId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            toast("请开启摄像头权限");
            return;
        }
        try {
            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    width, height, mVideoSize);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            configureTransform(width, height);
            mMediaRecorder = new MediaRecorder();
            mCameraManager.openCamera(mCameraId, mStateCallback, null);
        } catch (Exception e) {
            new AlertDialog.Builder(this)
                    .setMessage("打开摄像头失败，是否重试？")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openCameraById(mTextureView.getWidth(), mTextureView.getHeight(), mCameraId);
                        }
                    }).create().show();
        }
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    /*释放关闭相机*/
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /*开启摄像头预览*/
    private void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize || mVideoViewContainer.getVisibility() == View.VISIBLE) {
            return;
        }
        try {
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<Surface>();

            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewBuilder.addTarget(recorderSurface);

            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(VideoCaptureActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(KEY_RECORD_PARAM, recordParam.toJSONString());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    /*开始录制回调*/
    private void startRecordingVideo() {
        try {
            // UI
            mIsRecordingVideo = true;
            mChronometer.setText(null);
            mChronometer.setVisibility(View.VISIBLE);
            mChronometer.start();
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /*停止录制回调*/
    private void stopRecordingVideo() {
        // UI
        mIsRecordingVideo = false;
        mChronometer.stop();
        mChronometer.setVisibility(View.INVISIBLE);
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        toast("视频已保存" + mOutputFile.getAbsolutePath());
        playVideo();
    }

    /*显示录制的视频并播放*/
    private void playVideo() {
        mVideoViewContainer.setVisibility(View.VISIBLE);
        Uri uri = Uri.fromFile(mOutputFile);
        mVideoView.setVideoURI(uri);

        mVideoView.start();
    }

    /*隐藏视频播放显示重新录制*/
    private void onBackClick() {
        mVideoView.stopPlayback();
        mVideoViewContainer.setVisibility(View.GONE);
        startPreview();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoViewContainer.getVisibility() == View.VISIBLE) {
            mVideoView.resume();
        }
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            beginPreview();
        }
    }

    @Override
    public void onPause() {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            closeCamera();
            stopBackgroundThread();
        }
        super.onPause();
        if (mVideoViewContainer.getVisibility() == View.VISIBLE) {
            mVideoView.pause();
        }
    }

    public static class ErrorDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage("该设备不支持Camera2API")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    public static void startVideoCapture(Activity activity, String json, int requestCode) {
        Intent intent = new Intent(activity, VideoCaptureActivity.class);
        intent.putExtra(KEY_RECORD_PARAM, json);
        activity.startActivityForResult(intent, requestCode);
    }

    public static JSONObject obtain(Intent intent) {
        String json = intent.getStringExtra(KEY_RECORD_PARAM);
        return JSON.parseObject(json);
    }

    private void logError(String error) {
        toast(error);
        Log.e(TAG, error);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

}
