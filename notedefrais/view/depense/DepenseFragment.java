package com.example.notedefrais.view.depense;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.GenericEntity;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.IonBackPressed;
import com.example.notedefrais.view.FragmentBase;
import com.example.notedefrais.view.ndf.NdfFragment;
import com.example.notedefrais.viewmodel.depense.DepenseViewModel;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.notedefrais.view.ndf.NdfFragment.PROJECT;

public class DepenseFragment extends FragmentBase implements DepenseViewModel.IAction, IonBackPressed {

    public static final String PROJECT = "PROJECT";
    private DepenseAdapter adapter;
    private RecyclerView recyclerView;
    private CompositeDisposable disposable;
    private DepenseViewModel viewModel;
    public final static String TAG = DepenseFragment.class.getSimpleName();

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        disposable = new CompositeDisposable();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_depense, container, false);
        Objects.requireNonNull(getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);
        getActivity().setTitle(R.string.RENTS);
        recyclerView = v.findViewById(R.id.fragment_depense_rcv);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /* set vm | adapter */
        viewModel = new DepenseViewModel(this, disposable, ((GenericEntity)((getArguments() != null) ? getArguments().getSerializable(PROJECT) : null)));
        adapter = new DepenseAdapter(viewModel);
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


    private void displayData(ArrayList<PrismaGestionCo_NDF_depense> list)
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
            displayData(viewModel.getDepenses());
        }
    }


    /* intent manager*/
    public static void launchDetail(FragmentManager manager, Bundle args, GenericEntity entity)
    {
        DepenseFragment fragment = new DepenseFragment();
        args.putSerializable(NdfFragment.PROJECT, entity);
        fragment.setArguments(args);
        Helper.replaceMainFragment(manager, fragment, NdfFragment.TAG);
    }

    @Override
    public boolean onBackPressed()
    {
        return true;
    }
}
