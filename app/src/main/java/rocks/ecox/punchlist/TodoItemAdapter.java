package rocks.ecox.punchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TodoItemAdapter extends ArrayAdapter<Item> {

    public TodoItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, parent, false);

        TextView tvItem = (TextView) convertView.findViewById(R.id.tvItem);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        tvItem.setText(item.name);

        if(item.dueDate != null) {
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String dateString = df.format(item.dueDate);
            tvDueDate.setText("Due: " + dateString);
        } else {
            tvDueDate.setText("");
        }

        return convertView;
    }
}