package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private DataManager mDataManager;
    private boolean mCurrentEditMode = false;

    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_img) ImageView mUserProfileImage;
    @BindView(R.id.navigation_drawer) DrawerLayout mNavigationDrawer;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;

    @BindViews({R.id.userRating, R.id.userCodeLines, R.id.userProjects}) List<TextView> mUserValueViews;

    @BindView(R.id.userPhoneButton) ImageView mUserPhoneButton;
    @BindView(R.id.userEmailButton) ImageView mUserMailButton;
    @BindView(R.id.userVkButton) ImageView mUserVkButton;
    @BindView(R.id.userGitButton) ImageView mUserGitButton;

    private ImageView mMenuAvatarView;
    private TextView mUserNameTxt;
    private TextView mUserEmailTxt;

    @BindViews({R.id.userPhoneNumber, R.id.userEmail, R.id.userVkProfile, R.id.userGithubRepository, R.id.userBio}) List<EditText> mUserInfoViews;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        mDataManager = DataManager.getInstance();
        ButterKnife.bind(this);
        View mNavigationViewHeader =  getLayoutInflater().inflate(R.layout.drawer_header, mNavigationView);
        mMenuAvatarView = (ImageView) mNavigationViewHeader.findViewById(R.id.menu_avatar);
        mUserNameTxt = (TextView) mNavigationViewHeader.findViewById(R.id.user_name_txt);
        mUserEmailTxt = (TextView) mNavigationViewHeader.findViewById(R.id.user_email_txt);

        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mUserPhoneButton.setOnClickListener(this);
        mUserMailButton.setOnClickListener(this);
        mUserVkButton.setOnClickListener(this);
        mUserGitButton.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        initUserFields();
        initUserInfoValue();

        mUserInfoViews.get(0).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!ConstantManager.PHONE_PATTERN.matcher(editable.toString()).matches()) {
                    mUserInfoViews.get(0).setError(getString(R.string.validation_email));
                }
            }
        });

        mUserInfoViews.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!ConstantManager.EMAIL_ADDRESS_PATTERN.matcher(editable.toString()).matches()) {
                    mUserInfoViews.get(1).setError(getString(R.string.validation_phone));
                }
            }
        });

        if (savedInstanceState != null) {
            mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
            changeEditMode(!mCurrentEditMode);
        }
    }

    /**
     * Обработчик выбора пункта меню
     * @param item original: The menu item that was selected
     * @return original: boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Закрытие бокового меню по нажатию на кнопку "Назад".
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerVisible(mNavigationView)) {
            mNavigationDrawer.closeDrawer(mNavigationView);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserFields();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * Обработка событий по клику/нажатию
     * @param view Элемент, который получил клик/нажатие
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                changeEditMode(mCurrentEditMode);
                mCurrentEditMode = !mCurrentEditMode;
                break;

            case R.id.profile_placeholder:
                //TODO: 29.06.2016 сделать выбор откуда загружать фото
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.userPhoneButton:
                actionDial();
                break;
            case R.id.userEmailButton:
                actionSendTo();
                break;
            case R.id.userVkButton:
                actionViewHttp(mUserInfoViews.get(2));
                break;
            case R.id.userGitButton:
                actionViewHttp(mUserInfoViews.get(3));
                break;
        }
    }

    /**
     * Сохранение состояния. Модифицировано для сохранения режима редактирования.
     * @param outState original: Bundle in which to place your saved state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    /**
     * Показывает снэкбар
     * @param msg Отображаемое сообщение
     */
    private void showSnackbar(String msg){
        Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Настраиваем тулбар: Поведение тулбара, иконка меню
     */
    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mToolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Настраиваем Drawer: слушатель на пункты меню, скругление аватарки.
     */
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_avatar);
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserAvatar())
                .placeholder(R.drawable.menu_avatar) // TODO: 01.07.2016 сделать плейсхолдер и transform + crop
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        //Set it in the ImageView
                        RoundedAvatarDrawable rad = new RoundedAvatarDrawable(bitmap);
                        mMenuAvatarView.setImageDrawable(rad.mutate());
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {}
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });
        initUserName();
        initUserEmail();
    }

    /**
     * Получение результата из другой Activity (фото из камеры или галлереи)
     * @param requestCode Код запроса
     * @param resultCode Код результата
     * @param data Полученные данные
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                Uri selectedImage;
                if (resultCode == RESULT_OK && data != null) {
                    selectedImage = data.getData();

                    insertProfileImage(selectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if(resultCode == RESULT_OK && mPhotoFile != null) {
                    selectedImage = Uri.fromFile(mPhotoFile);

                    insertProfileImage(selectedImage);
                }
        }

    }

    /**
     * Переключение между режимом редактирования и режимом просмотра
     * @param mode Текущий режим. False - режим просмотра. True - режим редактирования.
     */
    private void changeEditMode(boolean mode){
        if (!mode) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }

            showProfilePlaceholder();
            lockToolbar();
            mUserInfoViews.get(0).requestFocus(); //фокус на поле телефон
            mToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserFields();
            }

            hideProfilePlaceholder();
            unlockToolbar();
            // TODO: 29.06.2016 избавиться от зачёркнутого, заменить на что-нибудь другое
            mToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        }
    }

    /**
     * Загрузка данных пользователя из PreferencesManager
     */
    private void initUserFields(){
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.user_bg) // TODO: 01.07.2016 сделать плейсхолдер и transform + crop
                .into(mUserProfileImage);
    }

    /**
     * Сохранение данных пользователя в PreferencesManager
     */
    private void saveUserFields(){
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue(){
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }

    private void initUserName(){
        mUserNameTxt.setText(mDataManager.getPreferencesManager().loadUserFullName());
        this.setTitle(mDataManager.getPreferencesManager().loadUserFullName());
    }

    private void initUserEmail(){
        mUserEmailTxt.setText(mDataManager.getPreferencesManager().loadUserProfileData().get(1));
    }
    /**
     * Загрузка фото из галереи
     */
    private void loadPhotoFromGallery(){
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Запуск камеры и получение снимка
     */
    private void loadPhotoFromCamera(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 30.06.2016 обработать ошибку
            }

            if(mPhotoFile != null) {
                // TODO: 30.06.2016 передать фотофайл в интент
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемые разренения", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }

    /**
     * Проверка разрешений
     * @param requestCode Код запроса
     * @param permissions Запрашиваемые разрешения
     * @param grantResults Полученные разрешения
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 01.07.2016 тут обрабатывается разрешение (получено)
            }
        }

        if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // TODO: 01.07.2016 тут обрабатывается разрешение (получено)
        }
    }

    /**
     * Прячет заглушку аватара при выходе из режима редактирования
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Показывает заглушку аватара при переключении в режим редактирования
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Блокирует тулбар в режиме редактирования
     */
    private void lockToolbar(){
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mToolbarLayout.setLayoutParams(mAppBarParams);
    }

    /**
     * Разблокирует турбар при выходе из режима редактирования
     */
    private void unlockToolbar(){
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mToolbarLayout.setLayoutParams(mAppBarParams);
    }

    /**
     *
     * @param id
     * @return Возвращает диалоговое окно
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO: 29.06.2016 заменить OnCreateDialog на что-то не устаревшее
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.change_photo));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch(choiceItem){
                            case 0:
                                //TODO: 29.06.2016 загрузить из галереи
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                //TODO: 29.06.2016 сделать снимок
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                //TODO: 29.06.2016 отменить
                                dialogInterface.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    /**
     * Создаёт файл для получения изображения с камеры
     * @return Возвращает файл изображения, куда в последствии будет загружено изображение с камеры
     * @throws IOException
     */
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Добавление изображения профиля
     * @param selectedImage Ссылка на изображение для профиля
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mUserProfileImage);
                // TODO: 01.07.2016 сделать плейсхолдер и transform + crop
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    /**
     * Запуск настройки приложения, для получения необходимых разрешений
     */
    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Запуск звонилки
     */
    public void actionDial(){
        String phoneNumber = mUserInfoViews.get(0).getText().toString();
        Intent phoneCall = new Intent(Intent.ACTION_DIAL);
        phoneCall.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(phoneCall);
    }

    /**
     * Запуск приложения для отправки сообщения
     */
    private void actionSendTo() {
        String mail = mUserInfoViews.get(1).getText().toString();
        Intent mailSend = new Intent(Intent.ACTION_SENDTO);
        mailSend.setData(Uri.parse("mailto:" + mail));
        startActivity(mailSend);
    }

    /**
     * Запуск браузера
     * @param editText поле, откуда берётся URL
     */
    private void actionViewHttp(EditText editText) {
        String text = editText.getText().toString();
        Intent search = new Intent(Intent.ACTION_VIEW);
        search.setData(Uri.parse("http:" + text));
        startActivity(search);
    }
}
