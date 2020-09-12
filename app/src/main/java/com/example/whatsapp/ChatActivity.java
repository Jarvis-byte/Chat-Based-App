package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
String activeUser="";
EditText chatEditText;
    ListView chatListView;
    android.os.Handler customHandler;
ArrayList<String> messages =new ArrayList<>();
ArrayAdapter arrayAdapter;
    public  void sendChat(View view)
    {
        chatEditText=findViewById(R.id.chatEditText);
        ParseObject message=new ParseObject("Message");

        final String messageContent= chatEditText.getText().toString();

        message.put("sender", ParseUser.getCurrentUser().getUsername());

        message.put("recipient",activeUser);

        message.put("message",messageContent);

        chatEditText.setText("");

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                   messages.add(messageContent);
                   arrayAdapter.notifyDataSetChanged();

                }
            }
        });
    }


public void setChat()
{

    chatListView = (ListView) findViewById(R.id.chatlistView);
    arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,messages);
    chatListView.setAdapter(arrayAdapter);
    ParseQuery<ParseObject> query1=new ParseQuery<ParseObject>("Message");
    query1.whereEqualTo("sender",ParseUser.getCurrentUser().getUsername());
    query1.whereEqualTo("recipient",activeUser);
    ParseQuery<ParseObject> query2=new ParseQuery<ParseObject>("Message");
    query2.whereEqualTo("recipient",ParseUser.getCurrentUser().getUsername());
    query2.whereEqualTo("sender",activeUser);
//Now Combining Both of these query into single query
    List<ParseQuery<ParseObject>> queries=new ArrayList<ParseQuery<ParseObject>>();
    queries.add(query1);
    queries.add(query2);
    ParseQuery<ParseObject>query=ParseQuery.or(queries);
    query.orderByAscending("createdAt");
    query.findInBackground(new FindCallback<ParseObject>() {
        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            if(e==null)
            {
                if(objects.size()>0){
                    messages.clear();
                    for (ParseObject message:objects)
                    {
                        String messageContent=message.getString("message");
                        if(!message.getString("sender").equals(ParseUser.getCurrentUser().getUsername()))
                        {
                            messageContent="> "+messageContent;
                        }
                        messages.add(messageContent);


                    }
                    arrayAdapter.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                }
            }
        }
    });

}
    private void scrollMyListViewToBottom() {
        chatListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chatListView.setSelection(arrayAdapter.getCount() - 1);
            }
        });
    }

    private Runnable updateTimerThread = new Runnable()
    {
        public void run()
        {
            setChat();
            scrollMyListViewToBottom();

            customHandler.postDelayed(this, 2000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent=getIntent();
      activeUser=intent.getStringExtra("username");
        setTitle("Chat With "+activeUser);
//        scrollMyListViewToBottom();
         setChat();

        customHandler = new android.os.Handler();
        customHandler.postDelayed(updateTimerThread, 0);


    }

}