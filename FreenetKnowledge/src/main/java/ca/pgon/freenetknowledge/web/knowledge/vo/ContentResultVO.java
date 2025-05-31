package ca.pgon.freenetknowledge.web.knowledge.vo;

/**
 * A virtual object for the content search result.
 */
public class ContentResultVO {
    private String link;
    private String description;

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
