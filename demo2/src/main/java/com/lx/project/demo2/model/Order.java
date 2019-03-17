package com.lx.project.demo2.model;

import java.io.Serializable;

public class Order implements Serializable {
    private static final long serialVersionUID = 9111357402963030257L;

    private String id;

    private String name;

    private String message_id;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageId() {
        return message_id;
    }

    public void setMessageId(String messageId) {
        this.message_id = messageId;
    }
}
