package com.example.notedefrais.view.selection;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notedefrais.R;
import com.example.notedefrais.model.utils.NdfLogger;
import java.util.ArrayList;

public class PictureSelectionAdapter extends RecyclerView.Adapter<PictureSelectionAdapter.Holder> {

    /** MARK : Properties */
    public static final String TAG = DepenseCategorySelectionAdapter.class.getSimpleName();
    private ArrayList<String> mList;
    private String imageString;
    private Context mContext;


    /** Constructor with singleton RowClickListener injection */
    public PictureSelectionAdapter(ArrayList<String> imageList)
    {
        if (imageList != null)
        {
            mList = imageList;
        }
    }


    /** Rcv callback */
    @Override
    public PictureSelectionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_picture, parent, false);
        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull PictureSelectionAdapter.Holder holder, int position)
    {
        try
        {
            dataBinding(holder, position);

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }


    @Override
    public int getItemCount()
    {
        try
        {
            return this.mList.size();

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
        return 0;
    }


    /** Bind data according with holder and customize view */
    private void dataBinding(Holder v, int i)
    {
        imageString = this.mList.get(i);

        v.image.setImageURI(Uri.parse(imageString));
    }


    /** Holder for adapter*/
    class Holder extends RecyclerView.ViewHolder
    {
        ImageView image;

        private Holder(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.picture_view);
        }
    }
}
