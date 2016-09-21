package rocks.ecox.punchlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText etEditText;
    String updatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String itemText = getIntent().getStringExtra("itemValue");
        etEditText = (EditText) findViewById(R.id.etEditText);
        etEditText.setText("");
        if(itemText != null) etEditText.append(itemText);
        Log.d("EDIT_FEATURE", "Received: " + itemText);
    }

    // Save the item and go back to Main activity
    public void saveItem(View view) {
        Intent intent = new Intent(EditItemActivity.this, MainActivity.class);
        updatedText = etEditText.getText().toString();
        int position = getIntent().getIntExtra("itemPosition", -1);
        intent.putExtra("updatedItem", updatedText);
        intent.putExtra("itemPosition", position);
        etEditText.setText("");
        Log.d("EDIT_FEATURE", "Sending: " + updatedText + " at position " + position + " to Main.");
        setResult(RESULT_OK, intent);
        finish();
    }
}
