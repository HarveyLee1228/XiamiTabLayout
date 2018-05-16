package com.harvey.xiamidemo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * <pre>
 *     author : Harvey
 *     time   : 2018/03/28
 *     desc   :
 * </pre>
 */
public class XiamiAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;

    public XiamiAdapter(Context context, FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        int type;
        switch (position) {
            case 0:
                type = 0;
                break;
            case 1:
                type = 1;
                break;
            case 2:
                type = 2;
                break;
            case 3:
                type = 3;
                break;
            default:
                type = 0;
                break;
        }
        return XiamiFragment.newInstance(type, getPageTitle(position).toString());
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "乐库";
            case 1:
                return "推荐";
            case 2:
                return "趴间";
            case 3:
                return "看点";
            default:
                return "";
        }
    }
}
