package org.newbees.estimotelab.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.newbees.estimotelab.MyApplication;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.model.BeaconMessage;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by erichua on 6/13/15.
 */
public class MessageDetailAdapter implements ListAdapter {
    private final LayoutInflater inflater;
    private List<BeaconMessage> allMessage;

    public MessageDetailAdapter(List<BeaconMessage> allMessage) {
        inflater = (LayoutInflater) MyApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.allMessage = allMessage;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        if (null == allMessage) {
            return 0;
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder vh;
        if (convertView != null) {
            view = convertView;
            vh = (ViewHolder) convertView.getTag();
        } else {
            view = this.inflater.inflate(R.layout.item_message, null);
            vh = new ViewHolder(view);
        }
        vh.msgTitleTv.setText(getItem(position).getMsgTitle());
        vh.msgDetailTv.setText(getItem(position).getMsgDetail());
        return view;
    }

    class ViewHolder {
        @InjectView(R.id.msgTitleTv)
        TextView msgTitleTv;

        @InjectView(R.id.msgDetailTv)
        TextView msgDetailTv;

        ViewHolder(View view){
            ButterKnife.inject(view);
            view.setTag(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
