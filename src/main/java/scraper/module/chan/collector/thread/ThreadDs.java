package scraper.module.chan.collector.thread;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Neo4j entity representing 4chan thread structure.
 */
@NodeEntity
public class ThreadDs {

    @GraphId
    private Long id;

    @GraphProperty(propertyName = "threadId")
    private String threadId;

    @GraphProperty(propertyName = "board")
    private String board;

    @GraphProperty(propertyName = "subject")
    private String subject;

    @RelatedTo(type = "CONTAINS", direction = Direction.INCOMING)
    protected Set<PostDs> posts = new HashSet<>();

    public ThreadDs() {
    }

    public ThreadDs(String threadId, String board, String subject) {
        this.threadId = threadId;
        this.board = board;
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getBoard() {
        return board;
    }

    public String getSubject() {
        return subject;
    }

    public Set<PostDs> getPosts() {
        return Collections.unmodifiableSet(posts);
    }

    public List<PostDs> getOrderedPosts() {
        List<PostDs> orderedPosts = new ArrayList<>(posts);
        Collections.sort(orderedPosts, (post1, post2) -> post1.getDate().compareTo(post2.getDate()));
        return orderedPosts;
    }

    public void addPosts(Collection<? extends PostDs> postsToAdd) {
        postsToAdd.forEach(this::addPost);
    }

    public void addPost(PostDs post) {
        if (this.posts.contains(post)) {
            return;
        }

        post.setThread(null);
        post.thread = this;
        this.posts.add(post);
    }

    public void removePost(PostDs post) {
        if (!this.posts.contains(post)) {
            return;
        }

        post.thread = null;
        this.posts.remove(post);
    }
}
