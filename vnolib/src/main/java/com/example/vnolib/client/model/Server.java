package com.example.vnolib.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Server {

    private int index;
    private String name;
    private String ip;
    private int port;
    private String description;
    private String link;
    private String arg14;
}
