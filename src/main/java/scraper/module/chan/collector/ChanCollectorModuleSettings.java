package scraper.module.chan.collector;

import scraper.module.core.properties.bool.BoolProperty;
import scraper.module.core.properties.string.StringProperty;

/**
 * Settings for {@link ChanCollectorWorker}.
 */
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

    /**
     * Gets name of the 4chan board whose threads should be extracted.
     *
     * @return name of the 4chan board
     */
    public String getBoardName() {
        return boardName;
    }

    /**
     * Sets name of the 4chan board whose threads should be extracted.
     *
     * @param boardName name of the 4chan board
     */
    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    /**
     * Gets flag indicating if worker should download files available in thread.
     *
     * @return <tt>true</tt> if worker should download files available in thread, <tt>false</tt> otherwise
     */
    public boolean getDownloadFiles() {
        return downloadFiles;
    }

    /**
     * Sets flag indicating if worker should download files available in thread.
     *
     * @param downloadFiles flag to set
     */
    public void setDownloadFiles(boolean downloadFiles) {
        this.downloadFiles = downloadFiles;
    }
}
