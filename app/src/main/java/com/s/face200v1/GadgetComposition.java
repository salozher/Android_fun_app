package com.s.face200v1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.face.Landmark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GadgetComposition extends AppCompatActivity {
    List<Landmark> landmarks;
    Canvas canvas;
    Paint myRectPaint = new Paint();
    Paint myCircPaint = new Paint();
    private static final int PERMISSION_REQUEST_CODE = 123;
    Bitmap fotka;
    Bitmap myBitmap;
    Bitmap mutableBitmap;
    Bitmap myNoseBitmap;
    Bitmap myBeardBitmap;
    Bitmap myGlassesBitmap;
    Bitmap myMustacheBitmap;
    Bitmap myHatBitmap;
    ArrayList<int[]> myList;
    MyListParse priletelParcel;
    BitmapFactory.Options options;
    ImageView myImageView;
    private static final String HELVETICA_FONT = "Helvetica";
    Uri passUriToShare;
    String lastSavedPhotoPath;
    File funPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadget_composition);
        funGadget();
    }

    public void openHat(View view) {
        Hat hat = new Hat(myList, canvas, myHatBitmap);
        hat.makeHat();
    }

    public void openGlass(View view) {
        Glasses glasses = new Glasses(myList, canvas, myGlassesBitmap);
        glasses.makeGlasses();
    }

    public void openNose(View view) {
        Nose nose = new Nose(myList, canvas, myNoseBitmap);
        nose.makeNose();
    }

    public void openMustache(View view) {
        Mustache mustache = new Mustache(myList, canvas, myMustacheBitmap);
        mustache.makeMustache();
    }

    public void openBeard(View view) {
        Beard beard = new Beard(myList, canvas, myBeardBitmap);
        beard.makeBeard();
    }

    private void funGadget() {
        Intent intent = getIntent();
        options = new BitmapFactory.Options();
        options.inMutable = true;
        myImageView = (ImageView) findViewById(R.id.fotkaImageView);
        try {
            priletelParcel = (MyListParse) getIntent().getParcelableExtra("sendListParsel");
            myList = priletelParcel.listParsel;
            Uri imageUri = intent.getParcelableExtra("fotka");
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            myBitmap = BitmapFactory.decodeStream(imageStream);
            mutableBitmap = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
            myNoseBitmap = BitmapFactory.decodeResource(
                    getApplicationContext().getResources(),
                    R.drawable.nose2);
            myBeardBitmap = BitmapFactory.decodeResource(
                    getApplicationContext().getResources(),
                    R.drawable.beard2);
            myGlassesBitmap = BitmapFactory.decodeResource(
                    getApplicationContext().getResources(),
                    R.drawable.glass1);
            myMustacheBitmap = BitmapFactory.decodeResource(
                    getApplicationContext().getResources(),
                    R.drawable.mustache1);
            myHatBitmap = BitmapFactory.decodeResource(
                    getApplicationContext().getResources(),
                    R.drawable.hat1);
            canvas = new Canvas(mutableBitmap);
            canvas.drawBitmap(mutableBitmap, 0, 0, null);
        } catch (Exception e) {
        }
        myImageView.setImageDrawable(new BitmapDrawable(getResources(), mutableBitmap));
    }

    public void saveFun(View view) {
        save();
    }

    private void save() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String myOutputFilename = new String("SergApp" + timeStamp + ".jpg");
        funPhotoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), myOutputFilename);
        myImageView.setDrawingCacheEnabled(true);
        myImageView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        myImageView.buildDrawingCache();
        Bitmap newBitmap = Bitmap.createBitmap(myImageView.getDrawingCache());
        myImageView.setDrawingCacheEnabled(false);
        myImageView.destroyDrawingCache();
        try {
            FileOutputStream outStream = new FileOutputStream(funPhotoFile);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outStream);
            outStream.flush();
            outStream.close();
            lastSavedPhotoPath = funPhotoFile.getAbsolutePath();
            MediaScannerConnection.scanFile(this, new String[]{funPhotoFile.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.d("Face211v-001", "image is saved in gallery and gallery is refreshed.");
                        }
                    }
            );
            Toast.makeText(getApplicationContext(), "Image Saved Sucessfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void share(View view) {
        addTextToBitmap();
        save();
        Intent newIntent = new Intent(this, Gadget_share.class);
        String myPath = funPhotoFile.getAbsolutePath();
        Uri uri = Uri.fromFile(funPhotoFile);
        newIntent.putExtra("passUriToShare", uri);
        startActivity(newIntent);
    }

    public void makeText(View view) {
        addTextToBitmap();
    }

    private String currentMonth() {
        String monthToday = "Ramadan";
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        System.out.println("sejchas mesjats  " + month);
        if (month == 0) {
            monthToday = "January";
        }
        if (month == 1) {
            monthToday = "February";
        }
        if (month == 2) {
            monthToday = "March";
        }
        if (month == 3) {
            monthToday = "April";
        }
        if (month == 4) {
            monthToday = "May";
        }
        if (month == 5) {
            monthToday = "June";
        }
        if (month == 6) {
            monthToday = "July";
        }
        if (month == 7) {
            monthToday = "August";
        }
        if (month == 8) {
            monthToday = "September";
        }
        if (month == 9) {
            monthToday = "October";
        }
        if (month == 10) {
            monthToday = "November";
        }
        if (month == 11) {
            monthToday = "December";
        }
        return monthToday;
    }

    private void addTextToBitmap() {
        int bitmapWidth = mutableBitmap.getWidth();
        System.out.println("bitmapWidth  " + bitmapWidth);
        int bitmapHeight = mutableBitmap.getHeight();
        int proportion = bitmapWidth / 700;
        System.out.println("bitmapHeight  " + bitmapHeight);
        System.out.println("proportion  " + proportion);
        Canvas pictureCanvas = new Canvas(mutableBitmap);
        Typeface tf = Typeface.create(HELVETICA_FONT, Typeface.BOLD);
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20 * proportion,
                getResources().getDisplayMetrics());
        Paint textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(tf);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint textPaintOutline = new Paint();
        textPaintOutline.setAntiAlias(true);
        textPaintOutline.setTextSize(textSize);
        textPaintOutline.setColor(Color.BLACK);
        textPaintOutline.setTypeface(tf);
        textPaintOutline.setStyle(Paint.Style.STROKE);
        textPaintOutline.setTextAlign(Paint.Align.CENTER);
        textPaintOutline.setStrokeWidth(8);
        float xPos = bitmapWidth / 2;
        float yPos = bitmapHeight - bitmapHeight / 25 * (float) proportion;
        pictureCanvas.drawText("Novi employee of " + currentMonth(), xPos, yPos, textPaintOutline);
        pictureCanvas.drawText("Novi employee of " + currentMonth(), xPos, yPos, textPaint);
    }
}