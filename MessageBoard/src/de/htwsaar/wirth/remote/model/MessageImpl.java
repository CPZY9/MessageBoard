package de.htwsaar.wirth.remote.model;

import javax.persistence.*;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import java.io.Serializable;
import java.util.UUID;
import java.util.Date;

/**
 *  Implements Message-Interface
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 09.02.17
 */
@Entity
@Table(name="messages")
public class MessageImpl implements Serializable, Message {

    private static final long serialVersionUID = -5415774293797687291L;
    @Column
    @Id
    private UUID id;
    @Column
    private String msg;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    @Column
    private String author;
    @Column
    private String group;
    @Column
    private boolean isSent;
    @Column
    private boolean isPublic;
    @Column
    private boolean isPublished;

    /**
     * Constructor
     * @param msg
     * @param author
     * @param group
     * @param isPublic
     */
    public MessageImpl(String msg, String author, String group, boolean isPublic) {
        this.id =  UUID.randomUUID();
        this.msg = msg;
        this.createdAt = new Date();
        this.author = author;
        this.group = group;
        this.isPublic = isPublic;
    }

    public MessageImpl() {}

    public UUID getID() {
        return id;
    }

    public String getGroup() { return group; }

    public boolean isPublic() { return isPublic; }

    public boolean isPublished() { return isPublished; }

    public String getMessage(){ return msg; }

    public String getAuthor() {
        return author;
    }

    public Date getCreatedAt() { return createdAt; }

    public Date getModifiedAt() { return modifiedAt; }

    public boolean isSent() { return isSent; }

    public void setSent(boolean sent) { isSent = sent; }

    public void setPublished(boolean published) { isPublished = published; }

    public void changeMessage(String msg){
        this.msg = msg;
        modifiedAt = new Date();
    }

    @Override
    public String toString() {
        return "MessageImpl{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", author='" + author + '\'' +
                ", group='" + group + '\'' +
                ", isSent=" + isSent +
                ", isPublic=" + isPublic +
                ", isPublished=" + isPublished +
                '}';
    }
}