package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    ArrayList<String> usernames=new ArrayList<String>();
    ArrayAdapter arrayAdapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
setTitle("User List");
        final ListView listView=findViewById(R.id.userListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("username",usernames.get(position));
                startActivity(intent);
            }
        });




        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,usernames);

        ParseQuery<ParseUser> query=ParseUser.getQuery();
        //Now we have to check that we will not be able to view the current user which is login into
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");//List will be in acending order.

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        for(ParseUser user:objects)
                        {
                            usernames.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                    }

                }else
                {
                    e.printStackTrace();
                }
            }
        });


    }
    }
