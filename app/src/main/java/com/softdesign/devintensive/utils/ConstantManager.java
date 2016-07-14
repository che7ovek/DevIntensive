package com.softdesign.devintensive.utils;

import android.content.IntentSender;

import java.util.regex.Pattern;

public interface ConstantManager {
    String TAG_PREFIX = "DEV ";

    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT_KEY = "USER_4_KEY";
    String USER_BIO_KEY = "USER_5_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    String USER_AVATAR_KEY = "USER_AVATAR_KEY";
    String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";
    String USER_ID_KEY = "USER_ID_KEY";

    String USER_RATING_VALUE = "USER_RATING_VALUE";
    String USER_CODE_LINES_VALUE = "USER_CODE_LINES_VALUE";
    String USER_PROJECT_VALUE = "USER_PROJECT_VALUE";

    String USER_FULL_NAME = "USER_FULL_NAME";
    String USER_FIRST_NAME = "USER_FIRST_NAME";
    String USER_SECOND_NAME = "USER_SECOND_NAME";

    String PARCELABLE_KEY = "PARCELABLE_KEY";

    int LOAD_PROFILE_PHOTO = 1;
    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 77;

    int PERMISSION_REQUEST_SETTINGS_CODE = 340;
    int CAMERA_REQUEST_PERMISSION_CODE = 341;

    Pattern PHONE_PATTERN = Pattern.compile(
            "\\+" +
                    "[0-9\\-\\s]{11,20}"
    );
    Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-]{3,64}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{2,64}" +
                    "\\." +
                    "[a-zA-Z0-9]{2,64}"
    );
}
