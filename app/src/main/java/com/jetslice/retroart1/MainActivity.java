package com.jetslice.retroart1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton mFab1,mFab2;
    public static final int PICK_IMAGE = 1;
    public static final int EDIT_IMAGE = 2;
    Bitmap bitmapx;
    public static final int RequestPermissionCode = 7;
    Uri uri = null;
    ArrayList<String> images;
    ImageView mBackroundBlurr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFab1= (FloatingActionButton) findViewById(R.id.gallery_fab_button);
        mFab2=(FloatingActionButton) findViewById(R.id.camera_fab_button);
        mFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editifyImage();

            }
        });
    }
    File[] listFile;

    public void getFromSdcard()
    {
        File file= new File("/sdcard/DCIM/Retroshots Pics");

        if (file.isDirectory())
        {
            listFile = file.listFiles();

            for (int i = 0; i < listFile.length; i++)
            {
                images.add(listFile[i].getAbsolutePath());
            }
        }
    }

    public void editifyImage() {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);


    }
    public void saveImageToExternal(Bitmap bm) throws IOException {
//Create Path to save Image
        File path = new File("/sdcard/DCIM/Retroshots Pics"); //Creates app specific folder
        path.mkdirs();
        String imgName = "Retroshot-" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(path, imgName); // Imagename.png

        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG,90,out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch (Exception e) {
        }
        throw new IOException();
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordAudioPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && RecordAudioPermission) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        images = new ArrayList<String>();// list of file paths
        getFromSdcard();
        mBackroundBlurr= (ImageView) findViewById(R.id.backround_blurr);
        int random= (int) (Math.random() * ( images.size()));

        if(images.size()==0){
            mBackroundBlurr.setImageResource(R.drawable.london3);
        }
        else{
            mBackroundBlurr.setImageURI(Uri.parse(images.get(random)));
        }
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, images));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    uri = data.getData();
                    Intent imageEditorIntent = new AdobeImageIntent.Builder(getApplicationContext()).setData(uri)
                            .withVibrationEnabled(true)
                            .build();


                    startActivityForResult(imageEditorIntent, EDIT_IMAGE);


                case EDIT_IMAGE:
                    Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
                    try {
                        bitmapx = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(String.valueOf(editedImageUri)));
                        saveImageToExternal(bitmapx);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


            }
        }
    }
            public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


}