package com.example.notedefrais.view.selection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.notedefrais.R;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.ImageSourceManager;
import com.example.notedefrais.view.NdfActivity;
import com.example.notedefrais.view.depense.DepenseDetailsActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PictureSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_URI_STRINGLIST = "EXTRA_URI_STRINGLIST";
    private ImageSourceManager mManager;
    private ArrayList<String> mData;
    private RecyclerView mRecyclerview;
    private PictureSelectionAdapter mAdapter;
    private ImageView mAddPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_selection);
        setTitle(getString(R.string.NEW_PICTURE));
        mManager = new ImageSourceManager(this, this);
        mData = getIntent().getStringArrayListExtra(PictureSelectionActivity.EXTRA_URI_STRINGLIST);
        mRecyclerview = findViewById(R.id.pictureSelectRcvLayout);

        mAdapter = new PictureSelectionAdapter(mData);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 3));

        mAddPicture = findViewById(R.id.button_add_picture);
        mAddPicture.setOnClickListener(this);
    }


    /* intent manger*/
    public static Intent launchDetail(Context context, ArrayList<String> uriStringList)
    {
        Intent intent = new Intent(context, PictureSelectionActivity.class);
        intent.putExtra(PictureSelectionActivity.EXTRA_URI_STRINGLIST, uriStringList);
        return intent;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_add_picture:
                mManager.getImageFromSource(0);
                break;
        }
    }

    /* this callback */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        // treatment of the image capture
        if(resultCode == RESULT_OK)
        {
            /* get picture from source start cropping */
            if(requestCode == NdfActivity.IMAGE_PICK_CAMERA_CODE)
            {
                CropImage.activity(mManager.getImageUri())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if(requestCode == NdfActivity.IMAGE_PICK_GALLERY_CODE)
            {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        /* treatment of the cropped image */
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE )
        {
            if(resultCode == RESULT_OK)
            {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if(result != null)
                {
                    Uri resultUri = result.getUri();

                    if(resultUri != null)
                    {
                        setResult(RESULT_OK, new Intent().setData(resultUri));
                        this.finish();
                    }
                }
            }
        }
    }
}
