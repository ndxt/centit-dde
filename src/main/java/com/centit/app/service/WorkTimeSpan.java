package com.centit.app.service;


public class WorkTimeSpan implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private String sign;
    private long nDays;
    private long nHours;
    private long nMinutes;


    public WorkTimeSpan() {

        nDays = 0;
        nHours = 0;
        nMinutes = 0;
    }

    public WorkTimeSpan(long days, long hours, long minutes) {

        nDays = days;
        nHours = hours;
        nMinutes = minutes;
    }

    public void fromString(String sTimeSpan) {
        char[] sc = sTimeSpan.toCharArray();
        int a;
        if (sTimeSpan.startsWith("-")) {
            a = 1;
            sign = "-";
        } else {
            a = 0;
        }

        for (int i = 0; i < sc.length; i++) {
            if (Character.isLetter(sc[i])) {
                switch (sc[i]) {
                    case 'D':
                        nDays = Long.parseLong(sTimeSpan.substring(a, i).trim());
                        break;
                    case 'H':
                        nHours = Long.parseLong(sTimeSpan.substring(a, i).trim());
                        break;
                    case 'M':
                        nMinutes = Long.parseLong(sTimeSpan.substring(a, i).trim());
                    default:
                        break;
                }
                a = i + 1;
            }
        }
    }

    @Override
    public String toString() {
        return sign + (nDays != 0 ? nDays + "D" : "") +
                (nHours != 0 ? nHours + "H" : "") +
                (nMinutes != 0 ? nMinutes + "M" : "");
    }

    public long getDays() {
        return nDays;
    }

    public void setDays(long days) {
        this.nDays = days;
    }

    public long getHours() {
        return nHours;
    }

    public void setHours(long hours) {
        this.nHours = hours;
    }

    public long getMinutes() {
        return nMinutes;
    }

    public void setMinutes(long minutes) {
        this.nMinutes = minutes;
    }
}
