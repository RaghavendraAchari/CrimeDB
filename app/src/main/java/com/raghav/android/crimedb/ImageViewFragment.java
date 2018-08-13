package com.raghav.android.crimedb;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Vighnesh on 13-08-2018.
 */

public class ImageViewFragment extends DialogFragment {
    private ImageView mImageView;
    private static final String FILE_PATH = "file_path";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.image_view_fragment,null);
        mImageView = (ImageView)v.findViewById(R.id.image_view);

        File file = (File) getArguments().getSerializable(FILE_PATH);
        //Uri uri = FileProvider.getUriForFile(getActivity(),"com.raghav.android.crimedb.fileprovider",file);

        Bitmap bitmap = BitmapFactory.decodeFile(file.toString());

        mImageView.setImageBitmap(bitmap);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(v);
        return dialog;
    }
    public static ImageViewFragment newInstance(File filePath){
        Bundle args = new Bundle();
        args.putSerializable(FILE_PATH,filePath);
        ImageViewFragment Fragment = new ImageViewFragment();
        Fragment.setArguments(args);
        return Fragment;
    }
}
