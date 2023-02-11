package xyz.udalny.vnomobile.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.model.Character;
import xyz.udalny.vnomobile.ClientHandler;
import xyz.udalny.vnomobile.R;
import xyz.udalny.vnomobile.exception.ResourceNotFoundException;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.RosterImage;

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
                Glide.with(parent.getContext())
                        .load(rosterImage.getOffIconFile())
                        .error(R.drawable.saul_icon)
                        .into(imageView);
            }
            else {
                Glide.with(parent.getContext())
                        .load(rosterImage.getOnIconFile())
                        .error(R.drawable.saul_icon)
                        .into(imageView);
            }
        }
        else {
            imageView.setImageResource(R.drawable.saul_icon);
        }
        return imageView;
    }

}
