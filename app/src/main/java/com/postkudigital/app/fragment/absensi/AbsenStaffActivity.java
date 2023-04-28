package com.postkudigital.app.fragment.absensi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.postkudigital.app.BaseApp;
import com.postkudigital.app.R;
import com.postkudigital.app.helpers.DHelper;
import com.postkudigital.app.json.GetCheckAbsenResponse;
import com.postkudigital.app.models.User;
import com.postkudigital.app.services.ServiceGenerator;
import com.postkudigital.app.services.api.UserService;
import com.postkudigital.app.utils.Log;
import com.postkudigital.app.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.postkudigital.app.helpers.Constants.TAG;

public class AbsenStaffActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {
    private Context context;
    private SessionManager sessionManager;
    private User user;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    public static final int REQUEST_CODE = 100;
    private SurfaceView surfaceView;
    private String[] neededPermissions = new String[]{Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE};
    File imageFileOwner;
    Button startBtn;
    RelativeLayout rlTake;
    TextView reset;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_staff);
        context = this;
        sessionManager = new SessionManager(context);
        user = BaseApp.getInstance(context).getLoginUser();
        surfaceView = findViewById(R.id.surfaceView);
        startBtn = findViewById(R.id.startBtn);
        rlTake = findViewById(R.id.rltake);
        reset = findViewById(R.id.reset);
        progressBar = findViewById(R.id.progressBar8);

        surfaceView.setVisibility(View.VISIBLE);
        setupSurfaceHolder();

        check();

        rlTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absen();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setVisibility(View.GONE);
                resetCamera();
            }
        });

    }

    private void check(){
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        service.checkAbsen(user.getId()).enqueue(new Callback<GetCheckAbsenResponse>() {
            @Override
            public void onResponse(Call<GetCheckAbsenResponse> call, Response<GetCheckAbsenResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatusCode() == 200){
                        startBtn.setText(response.body().getMessage());

                    }else {
                        DHelper.pesan(context, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCheckAbsenResponse> call, Throwable t) {

            }
        });
    }

    private void absen(){
        progressBar.setVisibility(View.VISIBLE);
        UserService service = ServiceGenerator.createService(UserService.class, sessionManager.getToken(), null, null, null);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("user", createPartFromString(user.getId()));
        map.put("toko", createPartFromString(sessionManager.getIdToko()));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), imageFileOwner);
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto", imageFileOwner.getName(), reqFile);
        service.absen(body, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        String msg = object.getString("msg");
                        DHelper.pesan(context, msg);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : neededPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                boolean shouldShowAlert = false;
                for (String permission : permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                }
                if (shouldShowAlert) {
                    showPermissionAlert(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                } else {
                    requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                }
                return false;
            }
        }
        return true;
    }

    private void showPermissionAlert(final String[] permissions) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_required);
        alertBuilder.setMessage(R.string.permission_message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(AbsenStaffActivity.this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(AbsenStaffActivity.this, R.string.permission_warning, Toast.LENGTH_LONG).show();
                        setViewVisibility(R.id.showPermissionMsg, View.VISIBLE);
                        return;
                    }
                }

                setupSurfaceHolder();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setViewVisibility(int id, int visibility) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    private void setupSurfaceHolder() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }



    public void captureImage() {
        if (camera != null) {
            camera.takePicture(null, null, this);
        }
    }

    private void startCamera() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.e(TAG, "img:" + data.toString());
        startBtn.setVisibility(View.VISIBLE);
        reset.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        imageFileOwner = DHelper.createTempFile(context, bitmap);
//        resetCamera();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        startCamera();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        resetCamera();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        releaseCamera();
    }
}