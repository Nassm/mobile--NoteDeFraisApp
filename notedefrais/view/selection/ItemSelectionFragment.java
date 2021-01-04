package com.example.notedefrais.view.selection;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.IonBackPressed;
import com.example.notedefrais.view.FragmentBase;
import java.util.ArrayList;
import java.util.Objects;

public class ItemSelectionFragment extends FragmentBase implements IonBackPressed {

    public static final String EXTRA_EXPENSE_REPORT = "mNdfs";
    public static final String TAG = ItemSelectionFragment.class.getSimpleName();

    private Bundle args;
    private ArrayList<PrismaGestionCo_NDF> mNdfs;
    private ArrayList<String> mNdfStringList;
    private Spinner mNdfSpinner;
    private String selectedNdf;
    private CardView newNdfCard;
    private CardView newDepenseCard;
    private SelectOptionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_item_selection, container, false);
        Objects.requireNonNull(getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);
        getActivity().setTitle(R.string.NEW_ENREGISTREMENT);
        mNdfSpinner = v.findViewById(R.id.itemSpinnerAddExpr);
        newNdfCard = v.findViewById(R.id.itemsAddExprCard);
        newDepenseCard = v.findViewById(R.id.itemsAddRentCard);
        mNdfStringList = new ArrayList<>(100);
        mListener = (SelectOptionListener) getActivity();
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        args = getArguments();
        mNdfs = ((ArrayList<PrismaGestionCo_NDF>) ((args != null) ? args.getSerializable(EXTRA_EXPENSE_REPORT) : null));

        displayDataAndSetListener(mNdfs);
    }
    private void displayDataAndSetListener(ArrayList<PrismaGestionCo_NDF> exp)
    {
        if(exp != null)
        {
            for (PrismaGestionCo_NDF ex : mNdfs)
            {
                mNdfStringList.add(ex.getNom());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_ndf_spinner, mNdfStringList);
        mNdfSpinner.setAdapter(adapter);

        if(mListener != null)
        {
            setNewExpenseReportListener(mListener);
            setNewRentListener(mListener);
        }
    }


    private void setNewExpenseReportListener(SelectOptionListener listener)
    {
        if (listener != null)
        {
            newNdfCard.setOnClickListener(v -> listener.onNewNdfSelection());
        }
    }

    private void setNewRentListener(SelectOptionListener listener)
    {
        if (listener != null)
        {
            newDepenseCard.setOnClickListener(v ->
            {
                selectedNdf = mNdfSpinner.getSelectedItem() != null ? mNdfSpinner.getSelectedItem().toString() : "";

                if(!selectedNdf.trim().isEmpty())
                {
                    listener.onNewDepenseSelection(App.get().getDB().prismagestionco_ndf_dao().getIdNdfByName(selectedNdf));
                } else
                    {
                        message(getString(R.string.AUCUNE_NOTE_DE_FRAIS));
                    }
            });
        }
    }

    public static void launchDetail(FragmentManager manager, Bundle args, ArrayList<PrismaGestionCo_NDF> entity)
    {
        ItemSelectionFragment fragment = new ItemSelectionFragment();
        args.putSerializable(ItemSelectionFragment.EXTRA_EXPENSE_REPORT, entity);
        fragment.setArguments(args);
        Helper.replaceMainFragment(manager, fragment, ItemSelectionFragment.TAG);
    }

    @Override
    public boolean onBackPressed()
    {
        return true;
    }


    /* callback for option selection */
    public interface SelectOptionListener
    {
        void onNewNdfSelection();

        void onNewDepenseSelection(int idNdf);
    }
}
