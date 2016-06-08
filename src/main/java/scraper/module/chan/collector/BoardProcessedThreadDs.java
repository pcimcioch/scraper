package scraper.module.chan.collector;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class BoardProcessedThreadDs {

	@GraphId
	private Long id;

	@GraphProperty(propertyName = "boadName")
	private String boardName;

	@GraphProperty(propertyName = "threadId")
	private String threadId;

	public BoardProcessedThreadDs() {

	}

	public BoardProcessedThreadDs(String boardName, String threadId) {
		this.boardName = boardName;
		this.threadId = threadId;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
}
