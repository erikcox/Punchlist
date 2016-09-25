package rocks.ecox.punchlist;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Table(name = "Items")
public class Item extends Model {
    @Column(name = "position", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    long position;

    @Column(name = "Name")
    public String name;

    @Column(name = "DueDate", index = true)
    public Date dueDate;

    // Constructor
    public Item() {
        super();
    }

    public Item(long position, String name) {
        super();
        this.position = position;
        this.name = name;
    }

    // Get all items
    public static List<Item> getAll() {
        return new Select()
                .from(Item.class)
                .execute();
    }

    // Delete item from DB
    public static void deleteItem(int position) {
        new Delete().from(Item.class).where("position = ?", position).execute();
        adjustPositions(position);
    }

    // Save items to DB
    public static void saveAllItems(List<Item> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (Item i : items) {
                Item item = i;
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    // Update positions after deletion
    private static void adjustPositions(int position) {
        List<Item> items = new Select()
                .from(Item.class)
                .where("position > ?", position)
                .execute();

        for (Item i : items) {
            i.position = position++;
            i.save();
        }
    }

    // Parse the date
    public static Date setDateFromString(String date) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
        simpleDate.setLenient(true);
        Date d = null;

        try {
            d = simpleDate.parse(date);
        } catch (ParseException e) {
            Log.e("DATE_FEATURE", e.toString());
        }
        return d;
    }
}
