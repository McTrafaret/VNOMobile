package xyz.udalny.vnolib.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Character {
    int charId;
    String charName;
    int taken;
}
