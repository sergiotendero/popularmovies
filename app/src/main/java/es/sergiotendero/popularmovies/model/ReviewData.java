package es.sergiotendero.popularmovies.model;

/**
 * Model class for reviews data
 */
public class ReviewData {

    private String author;
    private String content;

    public ReviewData() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return getAuthor() + ": " + getContent();
    }
}
