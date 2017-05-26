package com.traffic.wifiapp.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.mvp.presenter.LoginPresenter;
import com.traffic.wifiapp.mvp.view.LoginIView;
import com.traffic.wifiapp.utils.CommonUtils;
import com.traffic.wifiapp.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginIView {

    @Bind(R.id.exit_phone)
    EditText exitPhone;

    String phone = "";
    @Bind(R.id.tv_register)
    TextView tvRegister;

    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBeforeData() {
        tvRegister.setText(Html.fromHtml(getString(R.string.register)));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initAfterData() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new LoginPresenter(this, this);
    }

    @NeedsPermission({android.Manifest.permission.READ_PHONE_STATE})
    public void getDeviceIdPerssion(){
        mPresenter.login(phone);
    }

    @OnPermissionDenied({android.Manifest.permission.READ_PHONE_STATE})
    public void readPhoneRefuse(){
        showShortToast("拒绝了读取设备信息权限，您将无法进行登录");
    }

    @OnClick({R.id.btn_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                phone = exitPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showShortToast("请输入手机号");
                }
                if (!CommonUtils.isMobile(phone)) {
                    showShortToast("请输入正确的手机号");
                } else {
                    LoginActivityPermissionsDispatcher.getDeviceIdPerssionWithCheck(this);
                }
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onLoginSuccess() {
        showShortToast("登录成功");
        SPUtils.put(ConstantField.USER_NAME, phone);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

}
