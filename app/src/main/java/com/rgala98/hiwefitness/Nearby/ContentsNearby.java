package com.rgala98.hiwefitness.Nearby;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class ContentsNearby implements Parcelable {

    String gym_name;
    String image_url;
    String short_address;
    String full_address;
    String about_gym;
    String start_time;
    String end_time;
    String distance;

    public ContentsNearby(String gym_name, String image_url, String short_address, String full_address,String about_gym, String start_time, String end_time, String distance) {
        this.gym_name = gym_name;
        this.image_url = image_url;
        this.short_address = short_address;
        this.full_address = full_address;
        this.about_gym = about_gym;
        this.start_time = start_time;
        this.end_time = end_time;
        this.distance = distance;
    }

    protected ContentsNearby(Parcel in) {
        gym_name = in.readString();
        image_url = in.readString();
        short_address = in.readString();
        full_address = in.readString();
        about_gym = in.readString();
        start_time = in.readString();
        end_time = in.readString();
        distance = in.readString();

    }

    public static final Creator<ContentsNearby> CREATOR = new Creator<ContentsNearby>() {
        @Override
        public ContentsNearby createFromParcel(Parcel in) {
            return new ContentsNearby(in);
        }

        @Override
        public ContentsNearby[] newArray(int size) {
            return new ContentsNearby[size];
        }
    };

    public String getGym_name() {
        return gym_name;
    }

    public void setGym_name(String gym_name) {
        this.gym_name = gym_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getShort_address() {
        return short_address;
    }

    public void setShort_address(String short_address) {
        this.short_address = short_address;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAbout_gym() {
        return about_gym;
    }

    public void setAbout_gym(String about_gym) {
        this.about_gym = about_gym;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gym_name);
        parcel.writeString(image_url);
        parcel.writeString(short_address);
        parcel.writeString(full_address);
        parcel.writeString(about_gym);
        parcel.writeString(start_time);
        parcel.writeString(end_time);
        parcel.writeString(distance);

    }

    public static Comparator<ContentsNearby> distanceLowToHigh =new Comparator<ContentsNearby>() {
        @Override
        public int compare(ContentsNearby o1, ContentsNearby o2) {
            return (Integer.parseInt(o2.getDistance())<Integer.parseInt(o1.getDistance()) ? -1:
                    (Integer.parseInt(o2.getDistance())==Integer.parseInt(o1.getDistance()) ? 0 : 1));
        }
    };

}
