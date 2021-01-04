package com.example.notedefrais.view.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import com.example.notedefrais.model.utils.GenericAdapter;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.viewmodel.project.ProjectViewModel;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.Holder> implements GenericAdapter<PrismaGestionCo_projet> {

    /** MARK : Properties */
    private Context mContext;
    private ProjectViewModel mListener;
    private ArrayList<PrismaGestionCo_projet> mList;
    private PrismaGestionCo_projet mPrismaGestionCo_projet;
    public static final String TAG = ProjectAdapter.class.getSimpleName();


    /* Constructor with singleton RowClickListener injection */
    public ProjectAdapter(ProjectViewModel viewModel)
    {
        if (mListener == null)
        {
            mListener = viewModel;
        }
    }

    /* Generic adapter entity protocol */
    @Override
    public void setList(ArrayList<PrismaGestionCo_projet> entities)
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
    public void addToList(PrismaGestionCo_projet entity)
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
    public void removeFromList(PrismaGestionCo_projet entity)
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
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_project, parent, false);
        return new Holder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
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


    /* Holder for adapter*/
    class Holder extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageButton btnExp, btnRent, btnInfo;

        private Holder(@NonNull View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.item_project_name);
            btnExp = itemView.findViewById(R.id.item_project_exp);
            btnRent = itemView.findViewById(R.id.item_project_rent);

            btnExp.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                if (mListener != null && position != RecyclerView.NO_POSITION)
                {
                    mListener.onNdfClick(mList.get(position));
                }
            });

            btnRent.setOnClickListener(v ->
            {
                int position = getAdapterPosition();
                if (mListener != null && position != RecyclerView.NO_POSITION)
                {
                    mListener.onDepenseClick(mList.get(position));
                }
            });
        }
    }
    /* Bind data according with holder and customize view */
    private void dataBinding(Holder v, int i)
    {
        mPrismaGestionCo_projet = this.mList.get(i);

        v.name.setText(mPrismaGestionCo_projet.getNom());
    }


}
