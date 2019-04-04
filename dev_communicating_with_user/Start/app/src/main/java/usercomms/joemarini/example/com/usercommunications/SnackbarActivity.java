package usercomms.joemarini.example.com.usercommunications;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SnackbarActivity extends AppCompatActivity
    implements View.OnClickListener {

    private final String TAG="AUC-SNACKBAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar);

        findViewById(R.id.btnShowSnackbar).setOnClickListener(this);
        findViewById(R.id.btnShowActionSnackbar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowSnackbar:
                showSnackbar();
                break;
            case R.id.btnShowActionSnackbar:
                showActionSnackbar();
                break;
        }
    }

    private void showSnackbar() {
        // TODO: Create and show the snackbar

    }

    private void showActionSnackbar() {
        // TODO: Create a snackbar that has an action item

    }

}
