package com.s.face200v1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Nose {
    private ArrayList<int[]> listFaces;
    private Canvas canvas;
    private Bitmap inputBitmap;

    public Nose(ArrayList<int[]> listFaces, Canvas canvas, Bitmap inputBitmap) {
        this.listFaces = listFaces;
        this.canvas = canvas;
        this.inputBitmap = inputBitmap;
    }

    public void makeNose() {
        for (int[] face : listFaces) {
            int ugol = face[0];
            int eyeRightX = face[1];
            int eyeLeftX = face[3];
            int noseX = face[5];
            int noseY = face[6];
            float proportion = ((float) eyeLeftX - (float) eyeRightX) / (float) inputBitmap.getWidth() * (float) 0.8;
            Matrix myMatrix = new Matrix();
            float inputBitmapMiddleX = inputBitmap.getWidth() / 2;
            float inputBitmapMiddleY = inputBitmap.getHeight() / 2;
            float translateCorrectionX = (float) noseX - ((float) inputBitmap.getWidth() / (float) 2 * proportion);
            float translateCorrectionY = (float) noseY - ((float) inputBitmap.getHeight() / (float) 1.4 * proportion);
            myMatrix.setScale(proportion, proportion);
            myMatrix.preRotate(ugol, inputBitmapMiddleX, inputBitmapMiddleY);
            myMatrix.postTranslate(translateCorrectionX, translateCorrectionY);
            canvas.drawBitmap(inputBitmap, myMatrix, null);
        }
    }
}
