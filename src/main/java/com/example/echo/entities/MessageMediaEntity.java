package com.example.echo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "message_media")
public class MessageMediaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String mediaId;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageEntity message;


    public MessageMediaEntity(String url) {
        this.url = url;
    }

    public MessageMediaEntity() {

    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }
}
