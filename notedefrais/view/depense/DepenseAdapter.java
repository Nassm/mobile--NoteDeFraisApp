package com.example.notedefrais.view.depense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.utils.GenericAdapter;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.viewmodel.depense.GenericDepenseViewModel;
import com.example.notedefrais.model.utils.Helper;

import java.util.ArrayList;

public class DepenseAdapter extends RecyclerView.Adapter<DepenseAdapter.Holder> implements GenericAdapter<PrismaGestionCo_NDF_depense> {

    /* MARK : Properties */
    private Context mContext;
    private GenericDepenseViewModel mListener;
    private PrismaGestionCo_NDF_depense mDepense;
    private ArrayList<PrismaGestionCo_NDF_depense> mList;
    public static final String TAG = DepenseAdapter.class.getSimpleName();


    /* Constructor */
    public DepenseAdapter(GenericDepenseViewModel listener)
    {
        if (mListener == null)
        {
            mListener = listener;
        }
    }


    /* Generic adapter entity protocol */
    @Override
    public void setList(ArrayList<PrismaGestionCo_NDF_depense> entities)
    {
        try
        {
            mList = entities;
            notifyDataSetChanged();
        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }
    @Override
    public void addToList(PrismaGestionCo_NDF_depense entity)
    {
        try
        {
            mList.add(0, entity);
            notifyItemInserted(0);

        } catch (Exception e)
        {
            // logger
        }
    }
    @Override
    public void removeFromList(PrismaGestionCo_NDF_depense entity)
    {
        int pos = this.mList.indexOf(entity);
        try
        {
            this.mList.remove(entity);
            notifyItemRemoved(pos);
        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }


    /* Rcv callback */
    @NonNull
    @Override
    public DepenseAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_depense, parent, false);
        return new Holder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull DepenseAdapter.Holder holder, int position)
    {
        try {
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
            return mList.size();

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
        return 0;
    }


    /* Bind data and holder */
    public void dataBinding(Holder v, int i)
    {
        mDepense = this.mList.get(i);
        v.depLogo.setImageResource(R.drawable.ic_depense);
        v.name.setText(mDepense.getDescription());
        v.date.setText(Helper.formatDate(mDepense.getDate()));
        v.rentTTC.setText(Helper.formatDecimalTwoDigits(mDepense.getMontantTTC()));

    }
    public class Holder extends RecyclerView.ViewHolder
    {
        ImageView depLogo;
        TextView name, date, rentTTC;

        public Holder(@NonNull View itemView)
        {
            super(itemView);
            depLogo = itemView.findViewById(R.id.rentLogo);
            name = itemView.findViewById(R.id.rentName);
            date = itemView.findViewById(R.id.rentDate);
            rentTTC = itemView.findViewById(R.id.rentTTC);

            itemView.setOnClickListener(v ->
            {
                try
                {
                    int position = getAdapterPosition();

                    if (mListener != null && position != RecyclerView.NO_POSITION)
                    {
                        mListener.onRowClick(mContext, mList.get(position));
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
