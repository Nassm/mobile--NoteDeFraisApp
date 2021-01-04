package com.example.notedefrais.view.ndf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.notedefrais.App;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.view.ActivityBase;
import com.example.notedefrais.view.NdfActivity;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.GenericEntity;
import com.example.notedefrais.model.utils.ApprovalState;
import com.example.notedefrais.model.utils.ImageSourceManager;
import com.example.notedefrais.view.depense.DepenseAdapter;
import com.example.notedefrais.view.selection.DepenseCategorySelectionActivity;
import com.example.notedefrais.viewmodel.ndf.NdfDetailsDepenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;

public class NdfDetailsActivity extends ActivityBase implements View.OnClickListener, NdfDetailsDepenseViewModel.IAction {
    public static final String TAG = NdfDetailsActivity.class.getSimpleName();

    private DepenseAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ImageSourceManager mManager;
    private NdfDetailsDepenseViewModel mViewModel;
    public static final String EXTRA_NDF = "NDF";
    private CompositeDisposable disposable = new CompositeDisposable();

    private EditText mName;
    private Button mSubmitState;
    private FloatingActionButton mAddDepFab;
    private ImageView kraft;
    private ImageButton mBack, mDelNdf;
    private RelativeLayout mTotalDepLyt, mNoDepLyt;
    private TextView mState, mDepCount, mDepCountLbl, mAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndf_details);

        mRecyclerView = findViewById(R.id.exprcRentListRcvLayout);
        mTotalDepLyt = findViewById(R.id.exprcTotalRentsLayout);
        mNoDepLyt = findViewById(R.id.exprcNoRentLayout);
        mAddDepFab = findViewById(R.id.exprcAddRentButton);

        mName = findViewById(R.id.exprcName);
        mState = findViewById(R.id.exprcState);
        mDepCount = findViewById(R.id.exprcRentCount);
        mDepCountLbl = findViewById(R.id.exprcCountLabel);
        mAmount = findViewById(R.id.exprcAmount);
        mSubmitState = findViewById(R.id.exprcSubmit);
        kraft = findViewById(R.id.exprcKraft);

        mBack = findViewById(R.id.exprcReturn);
        mDelNdf = findViewById(R.id.exprcDelete);

        mManager = new ImageSourceManager(this, this);
        mViewModel = new NdfDetailsDepenseViewModel(this, disposable, ((PrismaGestionCo_NDF) getIntent().getSerializableExtra(EXTRA_NDF)));
        mAdapter = new DepenseAdapter(mViewModel);
        setClickListening(this);
    }

    private boolean isNameSet() {
        if (mName.getText().toString().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private void displayData(NdfDetailsDepenseViewModel viewModel) {
        mAdapter.setList(viewModel.getNdfDepenses());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void dataMappingTask(NdfDetailsDepenseViewModel viewModel) {
        mName.setText(viewModel.getNdf().getNom());
        mState.setText(viewModel.getNdf().getEtat());

        if (viewModel.getNdfDepenses() == null || viewModel.getNdfDepenses().size() == 0) // addbutton if no depense
        {
            mTotalDepLyt.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mNoDepLyt.setVisibility(View.VISIBLE);
        } else {
            if (viewModel.getNdfDepenses().size() == 1) {
                mDepCountLbl.setText(getResources().getString(R.string.RENT));
            } else if (viewModel.getNdfDepenses().size() > 1) {
                mDepCountLbl.setText(getResources().getString(R.string.RENTS));
            }

            mDepCount.setText(String.valueOf(viewModel.getNdfDepenses().size()));
            mAmount.setText(Helper.formatDecimalTwoDigits(viewModel.getNdf().getAmount()));
        }

        /* customization of expense report state */
        if (mState.getText().toString().equals(""))
        {
            mSubmitState.setText(getString(R.string.SOUMETTRE));
            kraft.setVisibility(View.GONE);
        } else if (mState.getText().toString().equals(ApprovalState.NOT_SEND.toString()))
        {
            mSubmitState.setText(getString(R.string.SOUMETTRE));
            mDelNdf.setVisibility(View.VISIBLE);
        } else if (mState.getText().toString().equals(ApprovalState.DENY.toString()) || mState.getText().toString().equals(ApprovalState.ACCEPTED.toString())) {
            mName.setFocusable(false);
            mName.setCursorVisible(false);
            mName.setEnabled(false);
            mName.setBackgroundColor(Color.TRANSPARENT);
            mSubmitState.setVisibility(View.GONE);
        } else {
            mSubmitState.setText(getString(R.string.MODIFIER));
        }
    }

    /* vm callback */
    @Override
    public void vmLoadingFinished(boolean isFinish) {
        if (isFinish) {
            dataMappingTask(mViewModel);
            displayData(mViewModel);
        }
    }

    @Override
    public void onNdfSubmit() {
        if (!isNameSet()) {
            mViewModel.getAction().message("Vous devez renseigner un nom");
            return;
        }
        try
        {
            if (mViewModel.getNdfDepenses() == null || mViewModel.getNdfDepenses().size() == 0) {
                if (mViewModel.getNdf().getNom() == null) {
                    mViewModel.getNdf().setIdSmartphone(UUID.randomUUID().toString());
                    mViewModel.getNdf().setIdPersonne(App.getUserId());
                    mViewModel.getNdf().setNumeroNote(Helper.generateRandomInteger());
                    mViewModel.getNdf().setNom(mName.getText().toString());
                    mViewModel.getNdf().setEtat(ApprovalState.NOT_SEND.toString());
                }

                Helper.generateDialogTwoAction(this, getString(R.string.ALERT_AJOUT_NOTE_VIDE_DEPENSE), (dialog, which) ->
                {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            try
                            {
                                long id = App.get().getDB().prismagestionco_ndf_dao().insert(mViewModel.getNdf());
                                mManager.getImageFromSource((int) id);
                            } catch (Exception e)
                            {
                                NdfLogger.e(TAG, e.getMessage());
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            try
                            {if (mViewModel.getNdf().getNom() != null && !mViewModel.getNdf().getNom().equals(mName.getText().toString())) {
                                mViewModel.getNdf().setNom(mName.getText().toString());
                            }
                                App.get().getDB().prismagestionco_ndf_dao().insert(mViewModel.getNdf()); // need to call remoteDataStore
                                startActivity(new Intent(this, NdfActivity.class));

                            } catch (Exception e)
                            {
                                NdfLogger.e(TAG, e.getMessage());
                            }

                            break;
                    }
                });
            }
        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onNdfUpdate() {
        Helper.generateDialogTwoAction(this, getString(R.string.MESSAGE_MAJ), (dialog, which) ->
        {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (mViewModel.getNdf().getNom() != null && !mViewModel.getNdf().getNom().equals(mName.getText().toString())) {
                        mViewModel.getNdf().setNom(mName.getText().toString());
                        App.get().getDB().prismagestionco_ndf_dao().insert(mViewModel.getNdf());
                    }
                    mViewModel.getAction().success(getString(R.string.MIS_A_JOUR));
                    startActivity(new Intent(this, NdfActivity.class));
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        });
    }

    @Override
    public void onNdfDelete()
    {
        Helper.generateDialogTwoAction(this, getString(R.string.MESSAGE_SUPPRESSION), (dialog, which) ->
        {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    App.get().getDB().prismagestionco_ndf_dao().delete(mViewModel.getNdf());
                    mViewModel.getAction().success(getString(R.string.SUPPRESSION));
                    startActivity(new Intent(this, NdfActivity.class));
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        });
    }

    @Override
    public void onDepenseAdd()
    {
        try
        {
            if(mViewModel.getNdf().getId() != 0)
            {
                mManager.getImageFromSource(mViewModel.getNdf().getId());
            } else
            {
                Helper.generateDialogTwoAction(this, getString(R.string.ALERT_NOTE_INEXISTANT), (dialog, which) ->
                {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            onNdfSubmit();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                });
            }
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exprcSubmit:
                switch (((Button) v).getText().toString())
                {
                    case "Soumettre":
                        onNdfSubmit();
                        break;

                    case "Submit":
                        onNdfSubmit();
                        break;

                    case "Presentar":
                        onNdfSubmit();
                        break;

                    case "Modifier":
                        onNdfUpdate();
                        break;

                    case "Edit":
                        onNdfUpdate();
                        break;

                    case "Canvi":
                        onNdfUpdate();
                        break;
                }
                break;

            case R.id.exprcAddRentButton:
                onDepenseAdd();
                break;

            case R.id.exprcReturn:
                onBackPressed();
                break;

            case R.id.exprcDelete:
                onNdfDelete();
                break;
        }
    }

    private void setClickListening(View.OnClickListener v) {
        mAddDepFab.setOnClickListener(v);
        mSubmitState.setOnClickListener(v);
        mBack.setOnClickListener(v);
        mDelNdf.setOnClickListener(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            /* get picture from source start cropping */
            if (requestCode == NdfActivity.IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(mManager.getImageUri())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == NdfActivity.IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        /* get cropped image */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (result != null) {
                    Uri resultUri = result.getUri();

                    if (resultUri != null) {
                        Intent i = new Intent(this, DepenseCategorySelectionActivity.class);
                        i.setData(resultUri);
                        startActivity(i);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case NdfActivity.CAMERA_REQUEST_PERMISSION_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        mManager.onCameraPicking();
                    } catch (Exception e) {
                        NdfLogger.e(TAG, e.getMessage());
                    }
                } else {
                    mViewModel.getAction().message(getString(R.string.ALERT_AUTORISATION_REQUIS));
                }
                break;
            }

            case NdfActivity.STORAGE_REQUEST_PERMISSION_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mManager.onGalleryImageSelecting();
                } else {
                    mViewModel.getAction().message(getString(R.string.ALERT_AUTORISATION_REQUIS));
                }
                break;
            }
        }
    }

    public static Intent launchDetail(Context context, GenericEntity entity) {
        Intent intent = new Intent(context, NdfDetailsActivity.class);
        intent.putExtra(NdfDetailsActivity.EXTRA_NDF, entity);
        return intent;
    }
}
