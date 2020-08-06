package com.example.indiabudgetgov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawerLayout;
    Toolbar toolbar;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         toolbar=findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
       // loadlocale();
       drawerLayout=findViewById(R.id.draw_layout);

        Button btnview =findViewById(R.id.btnview);
        Button btndownload=findViewById(R.id.btnpdf);

  StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
          StrictMode.setVmPolicy(builder.build());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
btnview.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        read_File(getApplicationContext(),"hbh1.pdf");
    }
});
        btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf
//                Uri path = Uri.parse("https://indiabudget.gov.in/doc/Budget_Speech.pdf");
//                Intent objIntent = new Intent(Intent.ACTION_VIEW);
//                objIntent.setDataAndType(path, "application/pdf");
//                objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
//                Intent intent=Intent.createChooser(objIntent,"Open Pdf");
//                startActivity(intent);//Starting the pdf viewer
//                new DownloadFile().execute("https://indiabudget.gov.in/doc/Budget_Speech.pdf", "Budget_Speech.pdf");

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED)
                    {
                        String Permissions[]={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(Permissions,1234);
                    }
                    else {
                        DownloadPdfs();
                    }

                }
                else {
                    DownloadPdfs();
                }



            }
        });

        ActionBarDrawerToggle mtoggle= new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();

    }

    private void read_File(Context context, String filename) {

        File yourFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()+File.separator+filename);
        Log.i("indiagov",Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath()+File.separator+filename);
        if (yourFile.exists()) {
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(Uri.fromFile(yourFile), "application/pdf");
            objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent intent = Intent.createChooser(objIntent, "Open Pdf");

            startActivity(intent);//Starting the pdf viewer}

    } else {
            Toast.makeText(getApplicationContext(),"File Does Not Exist,Download it First!",Toast.LENGTH_SHORT).show();
    }

    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }
    private void DownloadPdfs()
    {
        //https://file-examples-com.github.io/uploads/2017/10/file-sample_150kB.pdf
//https://vpn.nic.in/resources/manuals/smc-update-error.pdf
        String url="http://indiabudget.gov.in/doc/AFS/afs6.pdf";
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription("Downloading File....");
        request.allowScanningByMediaScanner();

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,title+".pdf");
       // request.setDestinationInExternalPublicDir("/data/data/com.example.indiabudgetgov/files/","a.pdf");
        //request.setDestinationInExternalFilesDir(getApplicationContext(),DIRECTORY_DOWNLOADS,"budgetspeech.pdf");
        DownloadManager downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        //request.setMimeType("application/pdf");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        Log.i("indiagov",String.valueOf(DIRECTORY_DOWNLOADS));
        downloadManager.enqueue(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.language_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.lang_english:
                setLocale("en");
                break;
            case R.id.lang_hindi:
                setLocale("hi");
                break;
            case R.id.share:
                ApplicationInfo api= getApplicationContext().getApplicationInfo();
                String apkpath=api.sourceDir;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("application/vnd.android.package-archive");
                String shareBody = "Here is the share content body";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                sharingIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(apkpath)));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setLocale(String lang)
    {
        Locale mylocale=new Locale(lang);
        Locale.setDefault(mylocale);
        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration conf=res.getConfiguration();
        conf.locale=mylocale;
        res.updateConfiguration(conf,dm);
        startActivity(new Intent(this,MainActivity.class));
        toolbar.setTitle(getResources().getString(R.string.app_name));
        //store preference of language selection
        SharedPreferences.Editor editor= getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("my_lang",lang);
        editor.apply();
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.keytobudgetdocuments:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new Frag_PdfView()).commit();
                title=getResources().getString(R.string.keytobudgetdocuments);
                break;
            case R.id.budgethightlights:
                title=getResources().getString(R.string.BudgetHighlights);
                break;
            case R.id.budgetspeech:
                title=getResources().getString(R.string.BudgetSpeech);
                break;
            case R.id.budgetatglance:
                title=getResources().getString(R.string.Budgetataglance);
                break;
            case R.id.annualfinancialstatement:
                title=getResources().getString(R.string.AnnualFinancialStatement);
                break;
            case R.id.Financebill:
                title=getResources().getString(R.string.Financialbill);
                break;
            case R.id.memorandum:
                title=getResources().getString(R.string.Memorandum);
                break;
            case R.id.ReceiptBudget:
                title=getResources().getString(R.string.ReceiptBudget);
                break;
            case R.id.ExpenditureBudget:
                title=getResources().getString(R.string.ExpenditureBudget);
                break;
            case R.id.CustomsNotifications:
                title=getResources().getString(R.string.CustomsNotifications);
                break;
            case R.id.themacroeconomicframeworkstatement:
                title=getResources().getString(R.string.themacroeconomicframeworkstatement);
                break;
            case R.id.mediumtermfiscal:
                title=getResources().getString(R.string.mediumtermfiscal);
                break;
            case R.id.Outputoutcome:
                title=getResources().getString(R.string.Outputoutcome);
                break;
            case R.id.implemenationofbudgetannouncements:
                title=getResources().getString(R.string.implemenationofbudgetannouncements);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
//   public void loadlocale()
//   {
//       SharedPreferences pref= getSharedPreferences("Settings",Activity.MODE_PRIVATE);
//       String language=pref.getString("my_lang","en");
//       setLocale(language);
//   }
}
