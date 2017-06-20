package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // declare fields here
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    // called by Android when the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        // the superclass' logic will be executed first
        super.onCreate(savedInstanceState);
        // Inflating the layout file from res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        // a way to reference the ListView
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        // initialization of list
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);

        // testing
        // items.add("weeeee I'm doing stuff!!!");
        // items.add("this is so much better than C!");

        // setup the listener on creation
        setupListViewListener();
    }

    public void onAddItem(View v) {
        // way of grabbing EditText
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);

        // grab its contents
        String itemText = etNewItem.getText().toString();

        // add our item to the list!!!
        itemsAdapter.add(itemText);

        writeItems();

        // Clear the EditText because we are done with it!
        etNewItem.setText("");

        // Let the user via a handy notification!
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    public void setupListViewListener() {
        // setup ListView's listener

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the item in the list at the index given by position
                items.remove(position);
                // notify the adapter that the underlying dataset changed
                itemsAdapter.notifyDataSetChanged();

                writeItems();

                // return true to tell the framework that the long click was consumed
                return true;
            }
        });
    }
    private File getDataFile()
    {
        return new File(getFilesDir(), "todo.txt");
    }

    // read the items from the file system
    private void readItems()
    {
        try {
            // create the array using the content in the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
            // just load an empty list
            items = new ArrayList<>();
        }
    }

    // write the items to the filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print the error to the console
            e.printStackTrace();
        }
    }
}
