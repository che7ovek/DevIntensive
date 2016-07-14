package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.softdesign.devintensive.R;

import java.util.List;

public class RepositoriesAdapter extends BaseAdapter{
    private List<String> mRepoList;
    private Context mContext;
    private LayoutInflater mInflater;

    public RepositoriesAdapter(Context context, List<String> repoList) {
        mRepoList = repoList;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRepoList.size();
    }

    @Override
    public Object getItem(int i) {
        return mRepoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View itemView = view;

        if (itemView == null) {
            itemView = mInflater.inflate(R.layout.item_repositories_list, viewGroup, false);
        }

        EditText repoName = (EditText) itemView.findViewById(R.id.github_et);
        repoName.setText(mRepoList.get(i));

        return itemView;
    }
}
