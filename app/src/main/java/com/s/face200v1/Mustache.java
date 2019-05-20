package com.s.face200v1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Mustache {
    private ArrayList<int[]> listFaces;
    private Canvas canvas;
    private Bitmap inputBitmap;

    public Mustache(ArrayList<int[]> listFaces, Canvas canvas, Bitmap inputBitmap) {
        this.listFaces = listFaces;
        this.canvas = canvas;
        this.inputBitmap = inputBitmap;
    }

    public void makeMustache() {
        for (int[] face : listFaces) {
            int ugol = face[0];
            int noseX = face[5];
            int noseY = face[6];
            int mouthLeftX = face[7];
            int mouthRightX = face[9];
            float proportion = ((float) mouthLeftX - (float) mouthRightX) / (float) inputBitmap.getWidth() * (float) 2.0;
            System.out.println("proportion " + proportion);
            Matrix myMatrix = new Matrix();
            float inputBitmapMiddleX = inputBitmap.getWidth() / 2;
            float translateCorrectionX = (float) noseX - ((float) inputBitmap.getWidth() / (float) 2 * proportion);
            float translateCorrectionY = (float) noseY - (float) 1 * proportion;
            myMatrix.setScale(proportion, proportion);
            myMatrix.preRotate(ugol, inputBitmapMiddleX, 0);
            myMatrix.postTranslate(translateCorrectionX, translateCorrectionY);
            canvas.drawBitmap(inputBitmap, myMatrix, null);
        }
    }
}
