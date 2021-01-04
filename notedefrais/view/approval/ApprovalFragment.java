package com.example.notedefrais.view.approval;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.view.FragmentBase;
import com.example.notedefrais.view.ndf.NdfDetailsActivity;
import com.example.notedefrais.viewmodel.approval.ApprovalViewModel;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class ApprovalFragment extends FragmentBase implements ApprovalViewModel.IAction{

    /* MARK : Properties */
    private ApprovalAdapter adapter;
    private RecyclerView recyclerView;
    private ApprovalViewModel viewModel;
    public static final String PROJECT = "PROJECT";
    private CompositeDisposable disposable = new CompositeDisposable();
    public static final String TAG = ApprovalFragment.class.getSimpleName();


    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        disposable = new CompositeDisposable();
    }
    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_approval, container, false);
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        getActivity().setTitle(R.string.APPROBATION);
        recyclerView = v.findViewById(R.id.fragment_approval_rcv);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /* set vm | adapter | fab click*/
        viewModel = new ApprovalViewModel(this, disposable);
        adapter = new ApprovalAdapter(viewModel);
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
            displayData(viewModel.getApproval());
        }
    }

    @Override
    public void startIntent(int tag, PrismaGestionCo_NDF ndf)
    {
        Intent intent;
        switch (tag)
        {
            case ApprovalViewModel.VIEW_APPROVAL:
                intent = NdfDetailsActivity.launchDetail(getContext(), ndf);
                startActivity(intent);
                break;
        }
    }
}