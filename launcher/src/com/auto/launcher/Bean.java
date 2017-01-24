package com.auto.launcher;

import android.os.Parcel;
import android.os.Parcelable;

public class Bean implements Parcelable {

    private int i;
    private String pkgname;

    public Bean() {

    }

    /**
     * @return the i
     */
    public int getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(int i) {
        this.i = i;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public Bean(Parcel in) {
        readFromParcel(in);
    }

    public static final Creator<Bean> CREATOR = new Creator<Bean>() {

        public Bean createFromParcel(Parcel in) {
            return new Bean(in);
        }

        public Bean[] newArray(int size) {
            return new Bean[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(i);
        dest.writeString(pkgname);
    }

    public void readFromParcel(Parcel in) {
        i = in.readInt();
        pkgname = in.readString();
    }
}
