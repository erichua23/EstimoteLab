package org.newbees.estimotelab.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import org.newbees.estimotelab.Const;
import org.newbees.estimotelab.MyApplication;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.model.BeaconMessage;
import org.newbees.estimotelab.model.CheckIn;
import org.newbees.estimotelab.utils.PC;
import org.newbees.estimotelab.utils.Toaster;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class CheckInActivity extends BaseActivity {

    private BeaconMessage msg;

    @InjectView(R.id.checkInTitleTv)
    TextView checkInTitleTv;

    @InjectView(R.id.checkInDescTv)
    TextView checkInDescTv;

    @InjectView(R.id.checkInBtn)
    Button checkInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        ButterKnife.inject(this);

        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        showDialog(DLG_LOADING);

        PC.checkExtra(this);

        msg = getIntent().getExtras().getParcelable(Const.EXTRA_KEY_MSG);

        checkInTitleTv.setText(msg.getMsgTitle());
        checkInDescTv.setText(msg.getMsgDetail());

        AVQuery<CheckIn> query = AVObject.getQuery(CheckIn.class);
        query.whereEqualTo("userName", MyApplication.getInstance().getCurrentUser().getUsername());
        query.whereEqualTo("message", msg.getObjectId());
        query.findInBackground(new FindCallback<CheckIn>() {
            @Override
            public void done(List<CheckIn> list, AVException e) {
                dismissDialog(DLG_LOADING);
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                if (list != null && list.size() > 0) {
                    // already checked in
                    updateView(State.CHECKED_IN);
                } else {
                    updateView(State.NOT_CHECK_IN);
                }
            }
        });
    }

    @OnClick(R.id.checkInBtn)
    public void onCheckInBtnClicked() {
        showDialog(DLG_LOADING);
        CheckIn checkIn = new CheckIn();
        checkIn.setMessageId(msg.getObjectId());
        checkIn.setUserName(MyApplication.getInstance().getCurrentUser().getUsername());
        checkIn.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                dismissDialog(DLG_LOADING);
                if (e != null) {
                    e.printStackTrace();
                    return;
                }

                updateView(State.CHECKED_IN);

            }
        });
    }

    enum State {
        NOT_CHECK_IN,
        CHECKED_IN
    }

    private void updateView(State state) {
        switch (state) {
            case NOT_CHECK_IN:
                checkInBtn.setEnabled(true);
                checkInBtn.setText(getString(R.string.check_in));
                break;
            case CHECKED_IN:
                checkInBtn.setEnabled(false);
                checkInBtn.setText(getString(R.string.already_checked));
                break;
        }

    }
}

