package com.xz.qrcodedemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.xz.proto.Person;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    private Button buttonTx;

    private ITxCall call = new ITxCall() {
        @Override
        public void onText(String text) {
            buttonTx.setText(text);
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int time = msg.arg1;
            if (time == 0) {
                // no thing
            } else {
                Message message = handler.obtainMessage(0, time - 1, 0);
                handler.sendMessageDelayed(message, 1000);
            }
            return true;
        }
    });

    private int time = 30;

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        WeakReference<MainActivity> activityWeakReference;

        public void setActivity(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            activityWeakReference.get();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new FragmentPagerAdapter()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                String[] cameraIdList = cameraManager.getCameraIdList();
                for (int i = 0; i < cameraIdList.length; i++) {
                    String id = cameraIdList[i];
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraManager.openCamera(id, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(@NonNull CameraDevice camera) {
                            try {
                                CaptureRequest request = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).build();
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDisconnected(@NonNull CameraDevice camera) {

                        }

                        @Override
                        public void onError(@NonNull CameraDevice camera, int error) {

                        }
                    }, new Handler());
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }


        buttonTx = findViewById(R.id.btn_tx);

        buttonTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Test test = new Test();
//                test.setCall(call);
//                test.getData();
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<InetAddress> list = Arrays.asList(InetAddress.getAllByName("www.baidu.com"));
                            Log.i("zzzzzzzz",list.size() + "");
                            for (int i =0;i < list.size();i++) {
                                InetAddress addr = list.get(i);
                                Log.i("zzzzzzzzzzzzzz",addr.getHostAddress() + "::" + addr.getHostName() + "::" + addr.getCanonicalHostName());
                            }
                            try {
                                Socket socket = new Socket(list.get(0), 0);
                                new Socket(Proxy.NO_PROXY);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();*/

//                startService(new Intent(MainActivity.this,MyService.class));
                Log.i("zzzzzzzzzz","on create service" + Thread.currentThread().getId() + "::" + Process.myPid());
                bindService(new Intent(MainActivity.this, MyService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        IMyAidlInterface iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
//                        iMyAidlInterface.basicTypes();
                        Log.i("zzzzzzzzzzzz","1111");
                        ((MyService.MyBinder)service).getData();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        /*try {
                            URL url = new URL("");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("POST");
                            urlConnection.setDoOutput(true); // 允许写出
                            urlConnection.setDoInput(true);  // 允许读入
                            urlConnection.setUseCaches(false);
                            urlConnection.connect();
                            urlConnection.getOutputStream();
                            int responseCode = urlConnection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                urlConnection.getInputStream();
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                }, Context.BIND_AUTO_CREATE);
                // 1s back finish
            }
        });

//        Message message = handler.obtainMessage(0, 30, 0);
//        handler.sendMessage(message);

        /*new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (time == 0) {
                    cancel();
                } else {

                    buttonTx.setText(--time + "");
                }
            }
        },0,1000);*/
//        handler.sendEmptyMessageDelayed(0,1000);
        //1s back

        /*SurfaceView surfaceView = new SurfaceView(this);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Camera open = Camera.open(0);

                try {
                    open.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                open.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        // data zxing
                    }
                });

                open.startPreview();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        SurfaceHolder holder = surfaceView.getHolder();*/


//        new IntentIntegrator(this)
//                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)// 扫码的类型,可选：一维码，二维码，一/二维码
//                .setPrompt("请对准二维码")// 设置提示语
//                .setCameraId(0)// 选择摄像头,可使用前置或者后置
//                .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
//                .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
//                .setOrientationLocked(false)
//                .initiateScan(); // `this` is the current Activity


        Person._Person person = Person._Person.newBuilder().setName("").setAccount("").setPassword("").build();

        byte[] bytes = person.toByteArray();

//        person.writeTo();

        try {
            Person._Person person1 = Person._Person.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}
