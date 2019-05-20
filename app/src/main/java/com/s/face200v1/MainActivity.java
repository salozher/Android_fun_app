package com.s.face200v1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private ImageView myImageView;
    Bitmap myBitmap;
    private final int PICK_IMAGE = 1;
    private final int MAKE_PHOTO = 2;
    ArrayList<int[]> listParsed;
    List<Landmark> landmarks;
    Canvas canvas;
    double ugol;
    float faceWidth;
    float faceHeight;
    float faceX;
    float faceY;
    Paint myRectPaint = new Paint();
    Paint myCircPaint = new Paint();
    static Bitmap mutableBitmap;
    String mCurrentPhotoPath;
    File imgFile;
    Uri imageUri;
    Uri photoUri;
    Uri fotkaUri;
    int landmarkType;
    boolean checkRe = false;
    boolean checkLe = false;
    boolean checkNb = false;
    boolean checkLm = false;
    boolean checkRm = false;
    boolean checkBm = false;
    boolean checkout = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myImageView = (ImageView) findViewById(R.id.imageView);
    }

    public void openPhoto(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        try {
            startActivityForResult(photoPickerIntent, PICK_IMAGE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void openCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                try {
                    startActivityForResult(intent, MAKE_PHOTO);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void makeFun(View view) {
        checkCompleteLandmarks();
        if (!checkout) {
            Toast.makeText(this, "No clear face on the photo! \nPlease pick or make another photo!", Toast.LENGTH_LONG).show();
            return;
        } else {
            MyListParse sendListParsel = new MyListParse(listParsed);
            Intent intent = new Intent(this, GadgetComposition.class);
            intent.putExtra("fotka", fotkaUri);
            intent.putExtra("sendListParsel", sendListParsel);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        resetCheckout();
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    imageUri = imageReturnedIntent.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    myBitmap = BitmapFactory.decodeStream(imageStream);
                    mutableBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
                } catch (FileNotFoundException e) {
                    System.out.println(e);
                    ;
                }
                fotkaUri = imageUri;
                analysePhoto();
            }
        }
        if (requestCode == MAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    imgFile = new File(mCurrentPhotoPath);
                    if (imgFile.exists()) {
                        Bitmap cameraBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        mutableBitmap = cameraBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                fotkaUri = photoUri;
                analysePhoto();
            } else {
                System.out.println("There is no photo made or opened");
            }
        }
    }

    private void analysePhoto() {
        myRectPaint.setStrokeWidth(3);
        myRectPaint.setColor(Color.GREEN);
        myRectPaint.setStyle(Paint.Style.STROKE);
        myCircPaint.setStrokeWidth(3);
        myCircPaint.setColor(Color.GREEN);
        myCircPaint.setStyle(Paint.Style.STROKE);
        canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(mutableBitmap, 0, 0, null);
        FaceDetector faceDetector = new FaceDetector.Builder(this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();
        Frame frame = new Frame.Builder().setBitmap(mutableBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        listParsed = new ArrayList<int[]>();
        for (int i = 0; i < faces.size(); i++) {
            listParsed.add(prepValues(faces.valueAt(i)));
        }
        myImageView.setImageDrawable(new BitmapDrawable(getResources(), mutableBitmap));
        faceDetector.release();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private int[] prepValues(Face face) {
        faceWidth = face.getWidth();
        faceHeight = face.getHeight();
        faceX = face.getPosition().x;
        faceY = face.getPosition().y;
        int markType;
        int cx;
        int cy;
        landmarks = face.getLandmarks();
        int[] listParam = new int[13];
        ugol = face.getEulerZ() * (-1);
        listParam[0] = (int) ugol;
        for (Landmark landmark : landmarks) {
            landmarkType = landmark.getType();
            markType = (int) (landmark.getType());
            cx = (int) (landmark.getPosition().x);
            cy = (int) (landmark.getPosition().y);
            if (markType == 10) {
                listParam[1] = cx;
                listParam[2] = cy;
            }
            if (markType == 4) {
                listParam[3] = cx;
                listParam[4] = cy;
            }
            if (markType == 6) {
                listParam[5] = cx;
                listParam[6] = cy;
            }
            if (markType == 5) {
                listParam[7] = cx;
                listParam[8] = cy;
            }
            if (markType == 11) {
                listParam[9] = cx;
                listParam[10] = cy;
            }
            if (markType == 0) {
                listParam[11] = cx;
                listParam[12] = cy;
            }
//            canvas.drawCircle(cx, cy, 6, myCircPaint);
            landmarkCheck();
        }
        return listParam;
    }

    private void landmarkCheck() {
        if (landmarkType == 10) {
            checkRe = true;
        }
        if (landmarkType == 4) {
            checkLe = true;
        }
        if (landmarkType == 6) {
            checkNb = true;
        }
        if (landmarkType == 5) {
            checkLm = true;
        }
        if (landmarkType == 11) {
            checkRm = true;
        }
        if (landmarkType == 0) {
            checkBm = true;
        }
    }

    private void resetCheckout() {
        checkBm = false;
        checkRm = false;
        checkNb = false;
        checkLe = false;
        checkLm = false;
        checkRe = false;
        checkout = false;
    }

    private void checkCompleteLandmarks() {
        if (checkBm && checkRm && checkNb && checkLe && checkLm && checkRe) {
            checkout = true;
        } else {
            System.out.println("not all landmarks are present! Pick or make another photo!");
        }
    }
}
