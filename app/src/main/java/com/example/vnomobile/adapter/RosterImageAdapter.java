package com.example.vnomobile.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.model.Character;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.exception.ResourceNotFoundException;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.RosterImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RosterImageAdapter extends BaseAdapter {

    public final static int ROSTER_IMAGE_WIDTH = 59;
    public final static int ROSTER_IMAGE_HEIGHT = 59;

    private final Client client;

    Map<String, RosterImage> nameToRosterImageMap;

    public RosterImageAdapter(DataDirectory dataDirectory) {
        client = ClientHandler.getClient();
        nameToRosterImageMap = new HashMap<>();
        try {
            List<RosterImage> imageList = dataDirectory.getRosterImages();
            for(RosterImage rosterImage : imageList) {
                nameToRosterImageMap.put(rosterImage.getCharacterName(), rosterImage);
            }
        } catch (ResourceNotFoundException e) {
            log.error("Get rosterImages: {}", e);
        }
    }

    @Override
    public int getCount() {
        return client.getNumOfCharacters();
    }

    @Override
    public Object getItem(int position) {
        return client.getCharacterByIndex(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(parent.getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
        }
        else {
            imageView = (ImageView) convertView;
        }
        Character character = client.getCharacterByIndex(position);
        if(nameToRosterImageMap.containsKey(character.getCharName())) {
            RosterImage rosterImage = nameToRosterImageMap.get(character.getCharName());
            if(character.getTaken() == 0) {
                imageView.setImageBitmap(rosterImage.getOffIcon());
            }
            else {
                imageView.setImageBitmap(rosterImage.getOnIcon());
            }
        }
        else {
            imageView.setImageResource(R.drawable.saul_icon);
        }
        return imageView;
    }

}