package ddwu.mobile.finalproject.ma01_20200554;

public class ReviewDTO {
    private long _id;
    private String title;
    private String date;
    private String photo;
    private String review;

    public ReviewDTO(String t, String d, String p, String r){
        title=t;
        date=d;
        photo=p;
        review=r;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
