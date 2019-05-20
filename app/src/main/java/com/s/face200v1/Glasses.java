package com.s.face200v1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import java.util.ArrayList;

public class Glasses {
    private ArrayList<int[]> listFaces;
    private Canvas canvas;
    private Bitmap inputBitmap;

    public Glasses(ArrayList<int[]> listFaces, Canvas canvas, Bitmap inputBitmap) {
        this.listFaces = listFaces;
        this.canvas = canvas;
        this.inputBitmap = inputBitmap;
    }
    public void makeGlasses() {
        for (int[] face : listFaces) {
            int ugol =  face[0];
            int eyeRightX =  face[1];
            int eyeRightY =  face[2];
            int eyeLeftX =  face[3];
            int mouthLeftX = face[7];
            int mouthRightX = face[9];
            float proportion = ((float)mouthLeftX - (float)mouthRightX) / (float)inputBitmap.getWidth() * (float) 3.0;
            Matrix myMatrix = new Matrix();
            float inputBitmapMiddleX = inputBitmap.getWidth() / 2;
            float inputBitmapMiddleY = inputBitmap.getHeight() / 2;
            float translateCorrectionX = (float)eyeRightX + (((float)eyeLeftX - (float)eyeRightX) / 2) - ((float)inputBitmap.getWidth() / (float)2 * proportion);
            float translateCorrectionY = (float)eyeRightY  - ((float)inputBitmap.getHeight() / (float)1.8 * proportion);
            myMatrix.setScale(proportion, proportion);
            myMatrix.preRotate(ugol, inputBitmapMiddleX, inputBitmapMiddleY);
            myMatrix.postTranslate(translateCorrectionX, translateCorrectionY);
            canvas.drawBitmap(inputBitmap, myMatrix, null);
        }
    }
}
