package rocks.ecox.punchlist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int EDIT_STATUS = 200;
    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Update the array items
        populateArrayItems();

        // Set up views
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etNewItem = (EditText) findViewById(R.id.etNewItem);

        // Remove item on long press
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        // Edit item on regular press
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("EDIT_FEATURE", "Editing item in position: " + position);
                Intent editItem = new Intent(MainActivity.this, EditItemActivity.class);
                editItem.putExtra("itemPosition", position);
                editItem.putExtra("itemValue", todoItems.get(position));
                Log.d("EDIT_FEATURE", "Sent position: " + position + " with Value:" + todoItems.get(position));
                startActivityForResult(editItem, EDIT_STATUS);
            }
        });
    }

    // Update edited item
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String editedItem = data.getStringExtra("updatedItem");
            int itemPosition = data.getExtras().getInt("itemPosition", -1);
            Log.d("EDIT_FEATURE", "Got edited item: " + editedItem + " at position: " + itemPosition);
            if (itemPosition > 0) {
                try {
                    todoItems.set(itemPosition, editedItem);
                    aToDoAdapter.notifyDataSetChanged();
                    writeItems();
                }catch (Exception e) {
                    Log.e("EDIT_FEATURE", "" + e);
                }
            }
        }
    }

    public void populateArrayItems() {
        readItems();
        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    // Read items from local file
    private void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
            todoItems = new ArrayList<String>();
            e.printStackTrace();
        }
    }

    // Write items to local file
    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View view) {
        aToDoAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        writeItems();
        hideKeyboard(view);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
