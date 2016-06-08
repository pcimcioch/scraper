package scraper.module.chan.collector;

import scraper.module.core.properties.bool.BoolProperty;
import scraper.module.core.properties.string.StringProperty;

public class ChanCollectorModuleSettings {

    @StringProperty(viewName = "Board Name", description = "Board Name", minLength = 2)
    private String boardName;

    @BoolProperty(viewName = "Download Files", description = "Check if You want to download files from posts")
    private boolean downloadFiles = true;

    public ChanCollectorModuleSettings() {
    }

    public ChanCollectorModuleSettings(String boardName, boolean downloadFiles) {
        this.boardName = boardName;
        this.downloadFiles = downloadFiles;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public boolean getDownloadFiles() {
        return downloadFiles;
    }

    public void setDownloadFiles(boolean downloadFiles) {
        this.downloadFiles = downloadFiles;
    }
}
