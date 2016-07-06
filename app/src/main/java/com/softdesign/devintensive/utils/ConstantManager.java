package com.softdesign.devintensive.utils;

import android.content.IntentSender;

public interface ConstantManager {
    String TAG_PREFIX = "DEV ";

    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY = "USER_1_KEY";
    String USER_MAIL_KEY = "USER_2_KEY";
    String USER_VK_KEY = "USER_3_KEY";
    String USER_GIT_KEY = "USER_4_KEY";
    String USER_BIO_KEY = "USER_5_KEY";
    String USER_PHOTO_KEY = "USER_PHOTO_KEY";
    int LOAD_PROFILE_PHOTO = 1;

    int REQUEST_CAMERA_PICTURE = 99;
    int REQUEST_GALLERY_PICTURE = 77;

    int PERMISSION_REQUEST_SETTINGS_CODE = 340;
    int CAMERA_REQUEST_PERMISSION_CODE = 341;
}
