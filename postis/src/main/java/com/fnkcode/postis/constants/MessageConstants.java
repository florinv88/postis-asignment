package com.fnkcode.postis.constants;

public final class MessageConstants {

    private MessageConstants(){}

    public static String VACATION_REQUEST = "The vacation requests found are : ";
    public static String NO_VACATION_REQUEST = "No vacation requests found.";
    public static String NR_DAYS_REMAINING = "Number of remaining vacation days : ";
    public static String SUCCESS = "SUCCESS";
    public static String FAILED = "Operation failed. Try again or contact the dev. team";
    public static String OVERLAPPING = "The overlapping requests found are : ";
    public static String REQUEST_NOT_FOUND = "The vacation request doesn't exists.";
    public static String REQUEST_NOT_UPDATEABLE = "The vacation request it's not in pending.";
    public static String VACATION_DATES_ERR = "Start date cannot be after the end date.";
    public static String VACATION_MAX_NUMBER_ERR = "The number of vacations days requested it's bigger then the remaining days.";
}
