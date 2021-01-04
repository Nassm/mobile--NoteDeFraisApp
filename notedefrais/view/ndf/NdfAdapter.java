package com.example.notedefrais.view.ndf;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.utils.GenericAdapter;
import com.example.notedefrais.model.utils.ApprovalState;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.viewmodel.ndf.NdfViewModel;

import java.util.ArrayList;

public class NdfAdapter extends RecyclerView.Adapter<NdfAdapter.Holder> implements GenericAdapter<PrismaGestionCo_NDF> {

    /* MARK : Properties */
    private Context mContext;
    private PrismaGestionCo_NDF mNdf;
    private NdfViewModel mListener;
    private ArrayList<PrismaGestionCo_NDF> mList;
    public static final String TAG = NdfAdapter.class.getSimpleName();


    /* Constructor with listener injection */
    public NdfAdapter(NdfViewModel listener)
    {
        if (mListener == null)
        {
            mListener = listener;
        }
    }


    /* Generic adapter entity protocol */
    @Override
    public void setList(ArrayList<PrismaGestionCo_NDF> entities)
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
    @Override
    public void addToList(PrismaGestionCo_NDF entity)
    {
        try
        {
            this.mList.add(0, entity);
            notifyItemInserted(0);

        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }
    @Override
    public void removeFromList(PrismaGestionCo_NDF entity)
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
    @Override
    public NdfAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_ndf, parent, false);
        return new Holder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull NdfAdapter.Holder holder, int position)
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


    /* Bind data according with holder and customize view */
    private void dataBinding(Holder v, int i)
    {
        mNdf = this.mList.get(i);
        v.name.setText(mNdf.getNom());

        /* customization of expense report state*/
        if(mNdf.getEtat().equals(ApprovalState.NOT_SEND.toString()))
        {
            v.state.setBackground(mContext.getResources().getDrawable(mContext.getResources().getIdentifier("non_transmis", "drawable", mContext.getPackageName())));

        } else if (mNdf.getEtat().equals(ApprovalState.SUBMITTED.toString()))
        {
            v.state.setBackground(mContext.getResources().getDrawable(mContext.getResources().getIdentifier("soumis_pour_approbation", "drawable", mContext.getPackageName())));

        } else if (mNdf.getEtat().endsWith(ApprovalState.IN_PROGRESS.toString()))
        {
            v.state.setBackground(mContext.getResources().getDrawable(mContext.getResources().getIdentifier("en_cours", "drawable", mContext.getPackageName())));

        } else if (mNdf.getEtat().equals(ApprovalState.ACCEPTED.toString()))
        {
            v.state.setBackground(mContext.getResources().getDrawable(mContext.getResources().getIdentifier(mNdf.getEtat().toLowerCase(), "drawable", mContext.getPackageName())));

        } else
            {
                v.state.setBackground(mContext.getResources().getDrawable(mContext.getResources().getIdentifier(mNdf.getEtat().toLowerCase(), "drawable", mContext.getPackageName())));

            }

        /* customization of rent count */
        v.depCount.setText(String.valueOf(mNdf.getDepenseCount()));
        if(mNdf.getDepenses() == null || mNdf.getDepenseCount().equals("0"))
        {
            v.depCount.setVisibility(View.GONE);
            v.depCountLbl.setText(mContext.getResources().getString(R.string.NO_RENT));

        } else if (mNdf.getDepenseCount().equals("1"))
        {
            v.depCountLbl.setText(mContext.getResources().getString(R.string.RENT));

        } else if (!mNdf.getDepenseCount().equals("0") && !mNdf.getDepenseCount().equals("1"))
        {
            v.depCountLbl.setText(mContext.getResources().getString(R.string.RENTS));
        }

        v.amount.setText(Helper.formatDecimalTwoDigits(mNdf.getAmount()));
    }

    /* Holder for adapter*/
    class Holder extends RecyclerView.ViewHolder
    {
        TextView name, depCount, depCountLbl, amount;
        ImageView state;

        private Holder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.exprName);
            depCount = itemView.findViewById(R.id.exprCount);
            depCountLbl = itemView.findViewById(R.id.exprCountLabel);
            amount = itemView.findViewById(R.id.exprAmount);
            state = itemView.findViewById(R.id.exprState);

            itemView.setOnClickListener(v ->
            {
                try
                {
                    int position = getAdapterPosition();

                    if (mListener != null && position != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemNdfClick(mList.get(position));
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
