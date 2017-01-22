package mobileapps.ipn.mx.loginremote;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class ReadComments extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // note that use read_comments.xml instead of our single_post.xml
        setContentView(R.layout.read_comments);
    }
}