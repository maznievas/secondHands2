package apobooking.apobooking.com.secondhands.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.SecondHandApplication;

/**
 * Created by procreationsmac on 24/07/2018.
 */

public class ImageFragment extends Fragment {

    private static final String REF = "ref";
    private static StorageReference storageReference;
    @Inject
    FirebaseStorage firebaseStorage;
    private String reference;

    public ImageFragment() {
        SecondHandApplication.getAppComponent().inject(this);
    }

    public static ImageFragment init(String storageReference1) {


        ImageFragment truitonFrag = new ImageFragment();

        //storageReference = storageReference1;

        Bundle args = new Bundle();
        args.putString(REF, storageReference1);
        truitonFrag.setArguments(args);

        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = getArguments().getString(REF, "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.sliding_images, container,
                false);
        ImageView imageView = (ImageView) layoutView.findViewById(R.id.image);

        storageReference = firebaseStorage
                .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + reference);

        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(storageReference)
               // .diskCacheStrategy(DiskCacheStrategy.NONE)
               // .skipMemoryCache(true)
                .into(imageView);

        return layoutView;
    }
}
