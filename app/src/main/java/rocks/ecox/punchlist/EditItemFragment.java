package rocks.ecox.punchlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class EditItemFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText etEditItem;
    private EditText etDueDateFrag;
    int position;
    TodoItemAdapter itemsAdapter;

    // DialogFragment empty constructor
    public EditItemFragment() {

    }

    // Setup edit fragment
    public static EditItemFragment newInstance(String itemName, String dueDate, int position, TodoItemAdapter itemsAdapter) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("itemName", itemName);
        args.putString("dueDate", dueDate);
        frag.setArguments(args);
        frag.position = position;
        frag.itemsAdapter = itemsAdapter;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);
        etEditItem = (EditText) rootView.findViewById(R.id.etEditItemFrag);
        etDueDateFrag = (EditText) rootView.findViewById(R.id.etDueDateFrag);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String itemText =  getArguments().getString("itemName", "");
        String dueDate = getArguments().getString("dueDate", "");

        etEditItem.setText(itemText);
        etEditItem.setSelection(etEditItem.getText().length());
        etDueDateFrag.setText(dueDate);

        Button saveButton = (Button)view.findViewById(R.id.btnSaveEditFrag);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onSave fragment click", etEditItem.getText().toString());

                Intent data = new Intent();
                data.putExtra("ItemText", etEditItem.getText().toString());

                ListView etItemText = (ListView) getActivity().findViewById(R.id.lvItems);
                Item item = (Item) etItemText.getItemAtPosition(position);
                item.name = etEditItem.getText().toString();

                if(etDueDateFrag.getText() != null && !etDueDateFrag.getText().toString().equals(""))
                    item.dueDate = Item.setDateFromString(etDueDateFrag.getText().toString());
                else
                    item.dueDate = null;

                item.save();
                itemsAdapter.notifyDataSetChanged();
                dismiss();
            }
        });

        EditText etDueDate = (EditText)view.findViewById(R.id.etDueDateFrag);
        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DueDateFragment();
                newFragment.setTargetFragment(EditItemFragment.this, 300);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

    }

    public void fromDate(String inputText) {
        etDueDateFrag.setText(inputText);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
}
