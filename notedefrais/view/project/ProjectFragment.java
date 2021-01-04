package com.example.notedefrais.view.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import com.example.notedefrais.view.FragmentBase;
import com.example.notedefrais.view.depense.DepenseFragment;
import com.example.notedefrais.view.ndf.NdfFragment;
import com.example.notedefrais.viewmodel.project.ProjectViewModel;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

public class ProjectFragment extends FragmentBase implements ProjectViewModel.IAction{

    private ProjectAdapter adapter;
    private RecyclerView recyclerView;
    private CompositeDisposable disposable;
    private ProjectViewModel viewModel;
    public final static String TAG = ProjectFragment.class.getName();

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        disposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_project, container, false);
        Objects.requireNonNull(getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);
        getActivity().setTitle(R.string.PROJET);
        recyclerView = v.findViewById(R.id.fragment_project_rcv);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /* set vm | adapter */
        viewModel = new ProjectViewModel(this, disposable);
        adapter = new ProjectAdapter(viewModel);
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

    private void displayData(ArrayList<PrismaGestionCo_projet> list)
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
            displayData(viewModel.getProjects());
        }
    }

    @Override
    public void startIntent(int tag, PrismaGestionCo_projet project)
    {
        Intent intent;
        switch (tag)
        {
            case ProjectViewModel.VIEW_NDF_TAG:
                NdfFragment.launchDetail(getFragmentManager(), new Bundle(), project);
                break;


            case ProjectViewModel.VIEW_DEPENSE_TAG:
                DepenseFragment.launchDetail(getFragmentManager(), new Bundle(), project);
                break;

        }
    }
}
