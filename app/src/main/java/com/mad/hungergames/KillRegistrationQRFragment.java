package com.mad.hungergames;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class KillRegistrationQRFragment extends Fragment {
    private SurfaceView surfaceView;
    private CameraSource cameraSource;
    private Context context;
    private Activity activity;
    private String killcode;

    private String BARCODE_ERROR = "Could not set up the detector!";
    private String KILLCODE_REGEX = "=";
    private String VICTIM_KILLCODE_TEXT = "Victim's killcode: ";
    private String NO_KILLCODE = "No killcode detected.";

    private String PERM_REQUIRED = "Permission Required";
    private String PERM_MESSAGE = "Camera is required to scan QR codes.";
    private String OK = "ok";
    private String CANCEL = "cancel";
    private String PERM_GRANTED = "Use camera permission granted";
    private String PERM_ERROR = "Mode is degraded";
    private String PERM_DENIED = "Use camera permission denied";

    private static final int USE_CAMERA_PERMISSIONS_REQUEST = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_kill_registration_qr, container,
                false);

        // Set up buttons and messages for QR code register.
        final TextView message = view.findViewById(R.id.message);
        final EditText usernameEt = view.findViewById(R.id.name);
        final Button confirmKillBtn = view.findViewById(R.id.button_confirm_kill);
        killcode = null;
        if (getActivity() != null){
            activity = getActivity();
            context = getActivity().getApplicationContext();
        }
        confirmKillBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = usernameEt.getText().toString();

                KillRegistration killRegistration = new KillRegistration(username, killcode, activity, context);
                killRegistration.registerKill();
            }
        });

        // Set up for camera use for QR code register.
        getPermissionToUseCamera();

        surfaceView = view.findViewById(R.id.camera_preview);

        final BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(context)
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();
        if(!barcodeDetector.isOperational()){
            message.setText(BARCODE_ERROR);
        }

        cameraSource = new CameraSource.Builder(context, barcodeDetector)
                .setRequestedPreviewSize(300, 300).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try {
                    cameraSource.start(holder);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size()!=0){
                    message.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            String url = barcodes.valueAt(0).rawValue;
                            // Reset killcode.
                            killcode = null;
                            String splitUrl[] = url.split(KILLCODE_REGEX);
                            if(splitUrl.length > 1) {
                                killcode = url.split(KILLCODE_REGEX)[1];
                                if(!(message.getText().toString()).equals(VICTIM_KILLCODE_TEXT + killcode)){
                                    vibrator.vibrate(500);
                                    message.setText(VICTIM_KILLCODE_TEXT + killcode);
                                }

                            }
                            else{
                                if(!(message.getText().toString()).equals(NO_KILLCODE)){
                                    vibrator.vibrate(250);
                                    message.setText(NO_KILLCODE);
                                }
                            }


                        }
                    });
                }
            }
        });

        return view;
    }

    /*
     * Asks the user for permission to use camera if it is not already given.
     */
    public void getPermissionToUseCamera() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied it.
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(activity).setTitle(PERM_REQUIRED)
                        .setMessage(PERM_MESSAGE)
                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.CAMERA},
                                        USE_CAMERA_PERMISSIONS_REQUEST);
                            }
                        })
                        .setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
            else{
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        USE_CAMERA_PERMISSIONS_REQUEST);
            }
        }
    }

    /*
     * Callback with the request from calling requestPermissions(...)
     * Displays toasts according to whether permission is granted or denied.
     * Changes setting if user grants permission.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == USE_CAMERA_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, PERM_GRANTED, Toast.LENGTH_SHORT).show();
            } else {
                // showRationale = false if user clicks Never Ask Again, otherwise true
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA);

                if (showRationale) {
                    Toast.makeText(context, PERM_ERROR, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, PERM_DENIED, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
