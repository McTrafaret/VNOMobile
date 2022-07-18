package xyz.udalny.vnolib.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Area {

    int locationId;

    String locationName;

    int locationPopulation;

    String backgroundNamePattern;

    String arg5;

    public void setPopulation(int newPopulation) {
        this.locationPopulation = newPopulation;
    }
}
