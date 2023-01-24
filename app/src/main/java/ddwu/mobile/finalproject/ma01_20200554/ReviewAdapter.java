package ddwu.mobile.finalproject.ma01_20200554;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ReviewAdapter extends CursorAdapter {
    static class ViewHolder {
        TextView title;
        TextView review;
        TextView date;
        ImageView image;

        public ViewHolder() {
            title = null;
            review = null;
            date = null;
            image = null;
        }
    }



    LayoutInflater inflater;
    int layout;

    public ReviewAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    @Override

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder();
        view.setTag(holder);
        return view;
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.title == null) {
            holder.title = view.findViewById(R.id.tvTitle);
            holder.date = view.findViewById(R.id.tvDate);
            holder.review = view.findViewById(R.id.tvReview);
            holder.image = view.findViewById(R.id.reviewImage);
        }

        holder.title.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.TITLE)));
        holder.review.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.REVIEW)));
        holder.date.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.DATE)));

        String image = cursor.getString(cursor.getColumnIndex(ReviewDBHelper.PHOTO));
        if (image != null) {
            holder.image.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex((ReviewDBHelper.PHOTO)))));
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.mukzzang);
            holder.image.setImageDrawable(drawable);
        }

    }

}
