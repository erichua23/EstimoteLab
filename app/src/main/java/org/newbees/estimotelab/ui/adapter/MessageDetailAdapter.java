package org.newbees.estimotelab.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

import org.newbees.estimotelab.MyApplication;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.model.BeaconMessage;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by erichua on 6/13/15.
 */
public class MessageDetailAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private List<BeaconMessage> allMessage;

    public MessageDetailAdapter(List<BeaconMessage> allMessage) {
        this.allMessage = allMessage;
        inflater = (LayoutInflater) MyApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return allMessage.size();
    }

    @Override
    public BeaconMessage getItem(int position) {
        return allMessage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {

        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_message, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.msgTitleTv.setText(getItem(position).getMsgTitle());
        holder.msgDetailTv.setText(getItem(position).getMsgDetail());

        holder.learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.msgTitleTv)
        TextView msgTitleTv;

        @InjectView(R.id.msgDetailTv)
        TextView msgDetailTv;

        @InjectView(R.id.learnMoreBtn)
        Button learnBtn;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
