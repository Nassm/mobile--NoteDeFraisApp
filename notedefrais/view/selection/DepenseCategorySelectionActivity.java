package com.example.notedefrais.view.selection;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_categorie;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.view.ActivityBase;
import com.example.notedefrais.view.NdfActivity;
import com.example.notedefrais.view.depense.DepenseDetailsActivity;
import com.example.notedefrais.viewmodel.selection.DepenseCategorySelectionViewModel;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DepenseCategorySelectionActivity extends ActivityBase implements DepenseCategorySelectionViewModel.IAction {

    /* MARK : Properties */
    private RecyclerView recyclerView;
    private CompositeDisposable disposable;
    private DepenseCategorySelectionViewModel viewModel;
    private DepenseCategorySelectionAdapter adapter;
    public static final String PictureAnalysisTask_WARMING_EXTRA = "WARMING";
    public static final String TAG = DepenseCategorySelectionActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depensecategory_selection);
        setTitle(getString(R.string.CATEGORIE_DEPENSE));
        this.disposable = new CompositeDisposable();
        this.recyclerView = findViewById(R.id.fragment_rentc_rcv);
        this.viewModel = new DepenseCategorySelectionViewModel(this, disposable);
        this.adapter = new DepenseCategorySelectionAdapter(viewModel);
    }

    public void displayData(ArrayList<PrismaGestionCo_NDF_depense_categorie> list)
    {
        try
        {
            adapter.setList(list);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
    }

    @Override
    public void vmLoadingFinished(boolean isFinish)
    {
        if (isFinish)
        {
            displayData(viewModel.getmDepenseCategories());
        }
    }

    @Override
    public void startIntent(int tag, PrismaGestionCo_NDF_depense_categorie dep)
    {
        Intent intent;
        switch (tag)
        {
            case DepenseCategorySelectionViewModel.SELECT_CATEGORIE:

                intent = new Intent(this, DepenseDetailsActivity.class);
                Uri uri = getIntent().getData();
                PrismaGestionCo_NDF_depense depObject = new PrismaGestionCo_NDF_depense();
                try
                {
                    depObject.setIdCategorie(dep.getId());
                    depObject.setType(dep.getNom());

                    if(uri != null)
                    {
                        new PictureAnalysisTask(this, uri, intent).doInBackGround(new CompositeDisposable(), depObject);
                    }
                    else
                        {
                            intent.putExtra(DepenseDetailsActivity.EXTRA_DEPENSE, depObject);
                            startActivity(intent);
                        }
                } catch (Exception ex)
                {
                    NdfLogger.e(TAG, ex.getMessage());
                }
                break;
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.depense_category_select_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Toasty.info(this, "Bouton retour", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rentcs_menu_action_search:
                Toasty.info(this, "Bouton rechercher", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onBackPressed()
    {
        Helper.generateDialogTwoAction(this, getString(R.string.ALERT_MODIFICATION_RISQUE_PERDU), (dialog, which) ->
        {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    startActivity(new Intent(this, NdfActivity.class));
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        });
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }

    private class PictureAnalysisTask
    {
        private Context mContext;
        private Bitmap mBitmap;
        private TextRecognizer mRecognizer;
        private Uri mUri;
        private Intent mIntent;
        private String description;

        public PictureAnalysisTask(Context context, Uri uri, Intent intent) throws FileNotFoundException
        {
            InputStream stream = getContentResolver().openInputStream(uri);
            Drawable drawable = Drawable.createFromStream(stream, uri.toString());
            this.mBitmap = ((BitmapDrawable) drawable).getBitmap();
            this.mRecognizer = new TextRecognizer.Builder(context).build();
            this.mUri = uri;
            this.mIntent = intent;
            this.mContext = context;
        }

        public void doInBackGround(CompositeDisposable disposable, PrismaGestionCo_NDF_depense depense)
        {
            disposable.add(Single.fromCallable(() ->
            {
                try
                {
                    Frame frame = new Frame.Builder().setBitmap(this.mBitmap).build();

                    final SparseArray<TextBlock> items = this.mRecognizer.detect(frame);

                    String text = retrieveTextFromImage(items);

                    ArrayList<Float> textFloatData = retrieveFloatFromText(text);

                    setDepenseDate(text, depense);

                    setDepenseDescription(depense);

                    setDepensePrices(textFloatData, depense);

                    return "";
                }
                catch (Exception e)
                {
                    NdfLogger.e(TAG, e.getMessage());
                    return null;
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((resultTask) ->
                    {
                        if (resultTask != null && resultTask.equals(""))
                        {
                            this.mIntent.setData(this.mUri);
                            this.mIntent.putExtra(DepenseDetailsActivity.EXTRA_DEPENSE, depense);

                            if((depense.getDate() == null) || depense.getDescription() == null || depense.getMontantHT() < 0.1)
                            {
                                this.mIntent.putExtra(PictureAnalysisTask_WARMING_EXTRA, getString(R.string.SCAN_ERROR));
                            }
                            this.mContext.startActivity(this.mIntent);

                        } else
                        {
                            error(getString(R.string.ERROR_GLOBAL));
                        }
                    }));
        }



        private String retrieveTextFromImage(SparseArray<TextBlock> item)
        {
            StringBuilder builder = new StringBuilder();

            if(item.size() != 0)
            {
                for (int i = 0; i < item.size(); ++i)
                {
                    builder.append(item.valueAt(i).getValue());
                }
                description = item.valueAt(0).getValue();
            }
            return builder.toString();
        }

        private ArrayList<Float> retrieveFloatFromText(String text)
        {
            ArrayList<Float> data = new ArrayList<>();

            Pattern innerMatcherPattern = Pattern.compile("(\\d*\\.\\d{2})$");

            Pattern pattern1 = Pattern.compile("([Ee&€%]\\s*\\d*\\s?\\.\\s?\\d{2}\\s*)");

            Matcher matcher1 = pattern1.matcher(text);

            while (matcher1.find())
            {
                Matcher match = innerMatcherPattern.matcher(matcher1.group());
                while (match.find())
                {
                    data.add(Float.valueOf(match.group()));
                }
            }
            return data;
        }

        private void setDepenseDate(String text, PrismaGestionCo_NDF_depense dep) throws ParseException
        {
            ArrayList<String> matchingDate = new ArrayList<>();
            Date dateObject = null;
            Pattern pattern = Pattern.compile("(?:0?[1-9]|[12][0-9]|3[01])([- /.])(?:0?[1-9]|1[012])\\1(?:19|20)?\\d\\d");
            Matcher matcher = pattern.matcher(text);

            while (matcher.find())
            {
                matchingDate.add(matcher.group());
            }

            if(matchingDate.size() != 0)
            {
                Date dateFound = Helper.formatStringToDate(matchingDate.get(0));
                if(dateFound.compareTo(new Date()) < 0)
                {
                    dateObject = dateFound;
                }
                /*
                for(String date : matchingDate)
                {
                    Date dateFound = Helper.formatStringToDate(date);
                    if(dateFound.compareTo(new Date()) < 0)
                    {
                        dateObject = dateFound;
                    }
                }
                */
            }
            if(dateObject != null)
            {
                dep.setDate(dateObject);
            }
        }

        private void setDepenseDescription(PrismaGestionCo_NDF_depense dep)
        {
            dep.setDescription(description);
        }

        private void setDepensePrices(ArrayList<Float> data, PrismaGestionCo_NDF_depense dep)
        {
            Float ttc = 0.0f; Float ht = 0.0f;;
            for(int i = 0; i < data.size(); ++i)
            {
                if(data.get(i) > ttc)
                {
                    ttc = data.get(i);

                    for (int j = 0; j < data.size(); ++j )
                    {
                        if(data.get(j) > ht && ttc > data.get(j))
                        {
                            ht = data.get(j);
                        }
                    }
                }
            }

            /* surchage rent object */
            dep.setMontantTTC(ttc);
            dep.setMontantHT(ht);
            dep.setMontantTVA((ttc - ht));
        }
    }
}
