package shreyansh.com.barcodebooks;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("myTag", rawResult.getText()); // Prints scan results
        Log.v("myTag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        final String isbn = rawResult.getText();
        AlertDialog.Builder addToListAlert = new AlertDialog.Builder(this);
        addToListAlert.setCancelable(false);
        addToListAlert.setMessage("ISBN: "+ isbn + ". Do you want to add this to list?");

        addToListAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //code to add isbn to the list
                listActivity.addToList(isbn);
                Toast.makeText(scanActivity.this,"ISBN:" + isbn + "added", Toast.LENGTH_LONG).show();
                mScannerView.resumeCameraPreview(scanActivity.this);
            }
        });

        addToListAlert.setNegativeButton("Scan Again", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                mScannerView.resumeCameraPreview(scanActivity.this);
                mScannerView.setResultHandler(scanActivity.this); // Register ourselves as a handler for scan results.
            }
        });

        addToListAlert.setNeutralButton("Add n See list", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                listActivity.addToList(isbn);
                Toast.makeText(scanActivity.this,"ISBN:" + isbn + "added", Toast.LENGTH_LONG).show();
                Intent i = new Intent(scanActivity.this, listActivity.class);
                startActivity(i);
            }
        });

        AlertDialog alertDialog = addToListAlert.create();
        alertDialog.show();

    }



}