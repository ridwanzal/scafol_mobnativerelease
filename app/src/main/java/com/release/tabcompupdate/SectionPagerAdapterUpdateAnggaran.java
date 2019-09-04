package com.release.tabcompupdate;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.release.R;
import com.release.fragment.FragmentEditKontrak;
import com.release.fragment.FragmentEditKontrakAnggaran;
import com.release.fragment.FragmentEditLokasi;
import com.release.fragment.FragmentProgress;
import com.release.fragment.FragmentProgressAnggaran;

public class SectionPagerAdapterUpdateAnggaran extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]
//            {R.string.update_kurvas,
//            R.string.update_editkontrak,
//            R.string.update_editlokasi,
//            R.string.update_progress,
//            R.string.update_penyediajasa,
//            R.string.update_upload,
//            };
            {
//          R.string.update_kurvas,
            R.string.update_editkontrak,
            R.string.update_progress,
//            R.string.update_penyediajasa,
//            R.string.update_upload,
            };
    private final Context mContext;

    public SectionPagerAdapterUpdateAnggaran(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0 :
//                return new FragmentKurvaS();
                return new FragmentEditKontrakAnggaran();
            case 1 :
                return new FragmentProgressAnggaran();
//            case 2 :
//                return new FragmentProgress();
//            case 3 :
//            return new FragmentPenyediaJasa();
//                return new FragmentUploadData(); // upload
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}
