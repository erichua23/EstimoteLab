package org.newbees.estimotelab.ui;

import android.os.Bundle;
import android.widget.TextView;


import org.newbees.estimotelab.MyApplication;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.model.UserInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SignInActivity extends BaseActivity {

    @InjectView(R.id.usernameTv)
    TextView usernameTv;

    @InjectView(R.id.passwordTv)
    TextView passwordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.signInBtn)
    public void onSignInBtnClicked() {
        if ("demo".equals(usernameTv.getText().toString())
                && "demo".equals(passwordTv.getText().toString())) {
            MyApplication.getInstance().setCurrentUser(new UserInfo(usernameTv.getText().toString()));

            launchActivity(MainActivity.class);
            finish();
            return;
        }
    }
}
