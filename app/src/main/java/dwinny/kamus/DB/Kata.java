package dwinny.kamus.DB;

import android.os.Parcel;
import android.os.Parcelable;

public class Kata implements Parcelable {
    private int id;
    private String kata, arti;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKata() {
        return kata;
    }

    public void setKata(String kata) {
        this.kata = kata;
    }

    public String getArti() {
        return arti;
    }

    public void setArti(String arti) {
        this.arti = arti;
    }

    public Kata() {
    }

    public Kata(String kata, String arti) {
        this.kata = kata;
        this.arti = arti;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.arti);
        dest.writeString(this.kata);
    }

    protected Kata (Parcel in){
        this.id = in.readInt();
        this.kata = in.readString();
        this.arti = in.readString();

    }
    public static final Parcelable.Creator<Kata> CREATOR = new Parcelable.Creator<Kata>(){
        @Override
        public Kata createFromParcel(Parcel source) {
            return new Kata();
        }

        @Override
        public Kata[] newArray(int size) {
            return new Kata[size];
        }
    };

}
