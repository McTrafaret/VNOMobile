package com.example.vnolib.client;

import com.example.vnolib.command.BaseCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnCommand {
    Class<? extends BaseCommand> value();
}
