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
    int position;
    TodoItemAdapter itemsAdapter;

    // DialogFragment empty constructor
    public EditItemFragment() {

    }

    // Setup edit fragment
    public static EditItemFragment newInstance(String itemName, int position, TodoItemAdapter itemsAdapter) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("itemName", itemName);
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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String itemText =  getArguments().getString("itemName", "");

        etEditItem.setText(itemText);
        etEditItem.setSelection(etEditItem.getText().length());

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

                item.save();
                itemsAdapter.notifyDataSetChanged();
                dismiss();
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }
}
