package com.harvey.xiamidemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class XiamiFragment extends Fragment {
    private static final String PAGE_TYPE = "type";
    private static final String PAGE_NAME = "name";
    private int pageType;
    private String pageName;

    public XiamiFragment() {
    }

    public static XiamiFragment newInstance(int type,String name) {
        XiamiFragment fragment = new XiamiFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_TYPE, type);
        args.putString(PAGE_NAME ,name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageType = getArguments().getInt(PAGE_TYPE);
            pageName = getArguments().getString(PAGE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_xiami, container, false);
        TextView tvContent = contentView.findViewById(R.id.tv_content);
        tvContent.setText(pageName);
        return contentView;
    }

}
