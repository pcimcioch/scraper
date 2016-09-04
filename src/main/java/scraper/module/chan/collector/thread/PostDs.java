package scraper.module.chan.collector.thread;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Neo4j entity representing post structure in the 4chan thread.
 */
@NodeEntity
public class PostDs {

    @GraphId
    private Long id;

    @Property(name = "author")
    private String author;

    @Property(name = "date")
    private Date date;

    @Property(name = "postId")
    private String postId;

    @Property(name = "comment")
    private String comment;

    @Property(name = "fileName")
    private String fileName;

    @Property(name = "md5")
    private String md5;

    @Relationship(type = "REPLAY", direction = Relationship.INCOMING)
    private Set<PostDs> repliedBy = new HashSet<>();

    @Relationship(type = "REPLAY", direction = Relationship.OUTGOING)
    private Set<PostDs> replyTo = new HashSet<>();

    @Relationship(type = "CONTAINS", direction = Relationship.OUTGOING)
    protected ThreadDs thread;

    public PostDs() {
    }

    public PostDs(String author, Date date, String postId, String comment, String fileName, String md5) {
        this.author = author;
        this.date = date == null ? null : new Date(date.getTime());
        this.postId = postId;
        this.comment = comment;
        this.fileName = fileName;
        this.md5 = md5;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date == null ? null : new Date(date.getTime());
    }

    public String getPostId() {
        return postId;
    }

    public String getComment() {
        return comment;
    }

    public String getFileName() {
        return fileName;
    }

    public Set<PostDs> getRepliedBy() {
        return Collections.unmodifiableSet(repliedBy);
    }

    public Set<PostDs> getReplyTo() {
        return Collections.unmodifiableSet(replyTo);
    }

    public ThreadDs getThread() {
        return thread;
    }

    public String getMd5() {
        return md5;
    }

    public void setThread(ThreadDs thread) {
        if (this.thread == thread) {
            return;
        }

        if (this.thread != null) {
            this.thread.removePost(this);
        }

        this.thread = thread;
        if (thread != null) {
            thread.posts.add(this);
        }
    }

    public void addReplyTo(PostDs post) {
        this.replyTo.add(post);
        post.repliedBy.add(this);
    }

    public void removeReplyTo(PostDs post) {
        this.replyTo.remove(post);
        post.repliedBy.remove(this);
    }

    public void addRepliedBy(PostDs post) {
        this.repliedBy.add(post);
        post.replyTo.add(this);
    }

    public void addRepliedBy(Collection<? extends PostDs> posts) {
        for (PostDs post : posts) {
            this.addRepliedBy(post);
        }
    }

    public void addReplyTo(Collection<? extends PostDs> posts) {
        for (PostDs post : posts) {
            this.addReplyTo(post);
        }
    }

    public void removeRepliedBy(PostDs post) {
        this.repliedBy.remove(post);
        post.replyTo.remove(this);
    }
}
