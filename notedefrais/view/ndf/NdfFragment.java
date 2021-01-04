package com.example.notedefrais.view.ndf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.utils.ApprovalState;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.view.FragmentBase;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.GenericEntity;
import com.example.notedefrais.view.selection.ItemSelectionFragment;
import com.example.notedefrais.viewmodel.ndf.NdfViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class NdfFragment extends FragmentBase implements NdfViewModel.IAction {

    /* MARK : Properties */
    private NdfAdapter adapter;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private NdfViewModel viewModel;
    public static final String PROJECT = "PROJECT";
    private CompositeDisposable disposable;
    public static final String TAG = NdfFragment.class.getSimpleName();


    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        disposable = new CompositeDisposable();
    }
    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_ndf, container, false);
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        recyclerView = v.findViewById(R.id.fragment_expensereport_rcv);
        getActivity().setTitle(R.string.NDF);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /* set vm | adapter | fab click*/
        viewModel = new NdfViewModel(this, disposable, ((GenericEntity)((getArguments() != null) ? getArguments().getSerializable(PROJECT) : null)));
        adapter = new NdfAdapter(viewModel);
        fab.setOnClickListener(v -> viewModel.onAddExpenseReportClick());
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        if (disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }


    private void displayData(ArrayList<PrismaGestionCo_NDF> list)
    {
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void vmLoadingFinished(boolean isFinish)
    {
        if (isFinish)
        {
            displayData(viewModel.getNoteDeFrais());
        }
    }
    @Override
    public void startIntent(int tag, PrismaGestionCo_NDF ndf, ArrayList<PrismaGestionCo_NDF> obj)
    {
        Intent intent;
        switch (tag)
        {
            case NdfViewModel.VIEW_NDF:
                intent = NdfDetailsActivity.launchDetail(getContext(), ndf);
                startActivity(intent);
                break;

            case NdfViewModel.ADD_NDF:
                if(obj == null || obj.size() == 0)
                {
                    obj = new ArrayList<>();
                    if(viewModel.getNoteDeFrais() != null)
                    {
                        for(PrismaGestionCo_NDF coNdf : viewModel.getNoteDeFrais())
                        {
                            if(!coNdf.getEtat().equals(ApprovalState.DENY.toString()))
                            {
                                obj.add(coNdf);
                            }
                        }
                    }
                }
                ItemSelectionFragment.launchDetail(getFragmentManager(), new Bundle(), obj);
                break;
        }
    }

    /* intent manager*/
    public static void launchDetail(FragmentManager manager, Bundle args, GenericEntity entity)
    {
        NdfFragment fragment = new NdfFragment();
        args.putSerializable(NdfFragment.PROJECT, entity);
        fragment.setArguments(args);
        Helper.replaceMainFragment(manager, fragment, NdfFragment.TAG);
    }
}