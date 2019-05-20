package com.s.face200v1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MyListParse implements Parcelable {
    public ArrayList<int[]> listParsel;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.listParsel);
    }

    public MyListParse(ArrayList<int[]> listParcel) {
        this.listParsel = listParcel;
    }

    private MyListParse(Parcel in) {
        this.listParsel = new ArrayList<int[]>();
        in.readList(this.listParsel, int[].class.getClassLoader());
    }

    public static final Parcelable.Creator<MyListParse> CREATOR = new Parcelable.Creator<MyListParse>() {
        @Override
        public MyListParse createFromParcel(Parcel source) {
            return new MyListParse(source);
        }

        @Override
        public MyListParse[] newArray(int size) {
            return new MyListParse[size];
        }
    };
}
