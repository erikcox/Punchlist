package rocks.ecox.punchlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.stetho.Stetho;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvItems;
    private ArrayList<Item> todoItems;
    private TodoItemAdapter aToDoAdapter;
    private EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Add Stetho for DB inspection
        Stetho.initializeWithDefaults(this);

        // Add icon to action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Set up views
        etNewItem = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        // Get items from DB
        getItems();
        aToDoAdapter = new TodoItemAdapter(this, todoItems);
        lvItems.setAdapter(aToDoAdapter);

        // Remove to-do item on long press
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                deleteItem(position);
                return true;
            }
        });

        // Edit item on regular press
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchEditItem(position);
            }
        });
    }

    // Update edited item from EditItemFragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String editedItem = data.getStringExtra("updatedItem");
            int itemPosition = data.getExtras().getInt("itemPosition", -1);
            if (itemPosition > 0) {
                try {
                    Item item = todoItems.get(itemPosition);
                    item.name = editedItem;
                    item.save();
                    aToDoAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("EDIT_FEATURE", "" + e);
                }
            }
        }
    }

    // Get items from DB
    private void getItems() {
        todoItems = (ArrayList) Item.getAll();
    }

    // Write all items to DB
    private void writeAllItems() {
        Item.saveAllItems(todoItems);
    }

    // Remove item from DB
    private void deleteItem(int position) {
        Item.deleteItem(position);
    }

    // onClick listener for Add button
    public void onAddItem(View view) {
        Item item = new Item();
        item.name = etNewItem.getText().toString();
        item.position = todoItems.size();
        writeAllItems();
        item.save();

        aToDoAdapter.add(item);
        hideKeyboard(view);
        etNewItem.setText("");
    }

    // Launch edit item fragment
    public void launchEditItem(int position) {
        String itemName = todoItems.get(position).name;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String itemDueDate = "";
        if(todoItems.get(position).dueDate != null)
            itemDueDate = df.format((todoItems.get(position).dueDate));

        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemDialog = EditItemFragment.newInstance(itemName, itemDueDate,position, aToDoAdapter);

        editItemDialog.show(fm, "fragment_edit_item");
    }

    // Hide the keyboard after adding an item
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
