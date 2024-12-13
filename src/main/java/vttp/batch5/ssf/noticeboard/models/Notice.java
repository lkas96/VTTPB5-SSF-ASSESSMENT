package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Notice {

    @NotEmpty(message = "Must have a title")
    @Size(min = 3, max = 128, message = "Title must be 3-128 characters long.")
    private String title;

    @NotEmpty(message = "Must have your email")
    @Email(message = "Please enter a valid email")
    private String poster; // email

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Must have a date in the future")
    private Date postDate; //posted date in format yyyy-MM-dd

    @NotEmpty(message = "Must include at least one category")
    private String categories;

    @NotEmpty(message = "Must have notice description or details")
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Notice(String title, String poster, Date postDate, String categories, String text) {
        this.title = title;
        this.poster = poster;
        this.postDate = postDate;
        this.categories = categories;
        this.text = text;
    }

    public Notice() {

    }

    @Override
    public String toString() {
        return title + "," + poster + "," + postDate + "," + categories + "," + text;
    }

}
