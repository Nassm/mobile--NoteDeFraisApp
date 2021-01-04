package com.example.notedefrais.view.selection;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_categorie;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.viewmodel.selection.DepenseCategorySelectionViewModel;

import java.util.ArrayList;

public class DepenseCategorySelectionAdapter extends RecyclerView.Adapter<DepenseCategorySelectionAdapter.Holder> {

    /** MARK : Properties */
    public static final String TAG = DepenseCategorySelectionAdapter.class.getSimpleName();
    private ArrayList<PrismaGestionCo_NDF_depense_categorie> mList;
    private DepenseCategorySelectionViewModel mListener;
    private PrismaGestionCo_NDF_depense_categorie mDepenseCategory;
    private Context mContext;


    /** Constructor with singleton RowClickListener injection */
    public DepenseCategorySelectionAdapter(DepenseCategorySelectionViewModel listener)
    {
        if (mListener == null)
        {
            mListener = listener;
        }
    }


    /** Generic adapter entity protocol */
    public void setList(ArrayList<PrismaGestionCo_NDF_depense_categorie> entities)
    {
        try
        {
            this.mList = entities;
            notifyDataSetChanged();
        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }


    /** Rcv callback */
    @Override
    public DepenseCategorySelectionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_depense_category, parent, false);
        return new Holder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull DepenseCategorySelectionAdapter.Holder holder, int position)
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
        mDepenseCategory = this.mList.get(i);

        v.layout.setBackgroundColor(Color.parseColor(mDepenseCategory.getColorSecondarySmartphone()));
        v.name.setText(mDepenseCategory.getNom());
        v.name.setBackgroundColor(Color.parseColor(mDepenseCategory.getColorPrimarySmartphone()));
        v.logo.setBackground(mContext.getResources().getDrawable(mContext.getResources().getIdentifier(mDepenseCategory.getNom().toLowerCase(), "drawable", mContext.getPackageName())));
    }


    /** Holder for adapter*/
    class Holder extends RecyclerView.ViewHolder
    {
        LinearLayout layout;
        TextView name;
        ImageView logo;

        private Holder(@NonNull View itemView)
        {
            super(itemView);
            layout = itemView.findViewById(R.id.depense_category_layout);
            name = itemView.findViewById(R.id.depense_category_name);
            logo = itemView.findViewById(R.id.depense_category_logo);

            itemView.setOnClickListener(v ->
            {
                try
                {
                    int position = getAdapterPosition();

                    if (mListener != null && position != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemDepenseCategoryClick(mList.get(position));
                    }
                }
                catch (Exception e)
                {
                    NdfLogger.e(TAG, e.getMessage());
                }
            });
        }
    }
}
