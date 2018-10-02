package apobooking.apobooking.com.secondhands.util.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import apobooking.apobooking.com.secondhands.util.ImageFragment;

/**
 * Created by procreationsmac on 24/07/2018.
 */

public class SlidingImageAdapter extends FragmentStatePagerAdapter {

    private List<String> imageList;
    private Context context;
    private FragmentManager fm;

    public SlidingImageAdapter(FragmentManager fm) {
        super(fm);
        imageList = new ArrayList<>();
        this.fm = fm;
    }


    @Override
    public int getCount() {
        if (imageList != null)
            return imageList.size();
        else return 0;
    }


    @Override
    public Fragment getItem(int position) {
        //Log.d("adapter", "getItem");
        return ImageFragment.init(imageList.get(position));
    }


    public void setImageList(/*List<StorageReference> list*/ List<String> list) {
        this.imageList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        //int position = imageList.indexOf(object);
       // return position == -1 ? POSITION_NONE : position;
        return POSITION_NONE;
    }
//


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        ImageFragment cf = (ImageFragment) object;
        fm.beginTransaction().remove(cf).commit();
//        mFrags.add(position, ContentFragment.newInstance(cf.getmParam1()));
//        mFrags.remove(position+1);
    }
}
