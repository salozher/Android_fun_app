package com.s.face200v1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Hat {
    private ArrayList<int[]> listFaces;
    private Canvas canvas;
    private Bitmap inputBitmap;

    public Hat(ArrayList<int[]> listFaces, Canvas canvas, Bitmap inputBitmap) {
        this.listFaces = listFaces;
        this.canvas = canvas;
        this.inputBitmap = inputBitmap;
    }

    public void makeHat() {
        for (int[] face : listFaces) {
            int ugol = face[0];
            int eyeRightX = face[1];
            int eyeRightY = face[2];
            int eyeLeftX = face[3];
            int mouthLeftX = face[7];
            int mouthRightX = face[9];
            double proportion = ((double) mouthLeftX - (double) mouthRightX) / (double) inputBitmap.getWidth() * (double) 3.2;
            Matrix myMatrix = new Matrix();
            float inputBitmapMiddleX = inputBitmap.getWidth() / 2;
            float inputBitmapMiddleY = inputBitmap.getHeight() / 2;
            double translateCorrectionX = (double) eyeRightX + ((((double) eyeLeftX - (double) eyeRightX) / 2) - (((double) inputBitmap.getWidth() / (double) 2)) * proportion);
            double translateCorrectionY = (double) eyeRightY - (((double) inputBitmap.getHeight() * proportion) * (double) 1.15);
            myMatrix.setScale((float) proportion, (float) proportion);
            System.out.println("proportion " + (float) proportion);
            myMatrix.preRotate(ugol, inputBitmapMiddleX, inputBitmapMiddleY * 2);
            myMatrix.postTranslate((float) translateCorrectionX, (float) translateCorrectionY);
            canvas.drawBitmap(inputBitmap, myMatrix, null);
        }
    }
}