package com.example.project2bookingsample.ui.summary;

import androidx.annotation.NonNull;

public class FutureBookingInfo {
    private String recCenterId;
    private String timeslot;
    private String isWaitList;
    private boolean active;

    public FutureBookingInfo(String recCenterId, String timeslot, String isWaitList) {
        this.recCenterId = recCenterId;
        this.timeslot = timeslot;
        this.isWaitList = isWaitList;
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

    @NonNull
    @Override
    public String toString() {
        String status;
        if (isWaitList.equals("true")) {
            status = "You are currently in the wait list.";
        } else if (isWaitList.equals("false")) {
            status = "You have already booked this slot.";
        } else {
            status = "status error";
        }

        return "Name of the recreation center: " + getRecCenter(recCenterId) +
                "\nYour booking starts from: " + timeslot +
                "\n" + status;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getWaitList() {
        return isWaitList;
    }

    public void setWaitList(String waitList) {
        isWaitList = waitList;
    }
}
