package com.example.project2bookingsample.ui.summary;

import androidx.annotation.NonNull;

public class PreviousBookingInfo {
    private String recCenterId;
    private String timeslot;

    public PreviousBookingInfo(String recCenterId, String timeslot) {
        this.recCenterId = recCenterId;
        this.timeslot = timeslot;
    }

    public String getRecCenterId() {
        return recCenterId;
    }

    public void setRecCenterId(String recCenterId) {
        this.recCenterId = recCenterId;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getRecCenter(String recCenterId) {
        if (Integer.parseInt(recCenterId) == 0) {
            return "Lyon Center";
        } else if (Integer.parseInt(recCenterId) == 1) {
            return "Village Center";
        } else if (Integer.parseInt(recCenterId) == 2) {
            return "HSC Center";
        } else {
            return "Conversion failed. Unrecognized RecCenterID";
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Name of the recreation center: " + getRecCenter(recCenterId) +
                "\nYour booking started from: " + timeslot;
    }
}
