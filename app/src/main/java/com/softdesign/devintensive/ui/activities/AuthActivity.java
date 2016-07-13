package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.login_button) Button mSignIn;
    @BindView(R.id.forget_pass) TextView mRememberPass;
    @BindView(R.id.login) EditText mLogin;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mDataManager = DataManager.getInstance();

        ButterKnife.bind(this);

        mRememberPass.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                signIn();
                break;
            case R.id.forget_pass:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar (String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelRes userModel) {
        showSnackbar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);

        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);

    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar(getString(R.string.error_wrongLoginPassword));
                    } else {
                        showSnackbar(getString(R.string.error_someError));
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 09.07.2016 обработать ошибки ретрофита
                }
            });
        } else {
            showSnackbar("Сеть на данный момент не доступна. Попробуйте позже.");
        }
    }

    private void saveUserValues(UserModelRes userModel) {
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()

        };

        List<String> userInfo = new ArrayList<>();
        userInfo.add(userModel.getData().getUser().getContacts().getPhone());
        userInfo.add(userModel.getData().getUser().getContacts().getEmail());
        userInfo.add(userModel.getData().getUser().getContacts().getVk());
        userInfo.add(userModel.getData().getUser().getRepositories().getRepo().get(0).getGit());
        userInfo.add(userModel.getData().getUser().getPublicInfo().getBio());

        List<String> userName = new ArrayList<>();
        userName.add(userModel.getData().getUser().getFirstName());
        userName.add(userModel.getData().getUser().getSecondName());
        userName.add(userName.get(0) + " " + userName.get(1));

        Uri userPhoto = Uri.parse(userModel.getData().getUser().getPublicInfo().getPhoto());
        Uri userAvatar = Uri.parse(userModel.getData().getUser().getPublicInfo().getAvatar());

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);
        mDataManager.getPreferencesManager().saveUserProfileData(userInfo);
        mDataManager.getPreferencesManager().saveUserFulName(userName);
        mDataManager.getPreferencesManager().saveUserPhoto(userPhoto);
        mDataManager.getPreferencesManager().saveUserAvatar(userAvatar);

    }
}
