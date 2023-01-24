package ddwu.mobile.finalproject.ma01_20200554;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReviewAddActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 200;
    private String mCurrentPhotoPath;

    ReviewDBHelper helper;
    EditText etName;
    EditText etAddress;
    EditText reviewDate;
    EditText reviewTitle;
    EditText review;
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        helper = new ReviewDBHelper(this);
        etName = (EditText) findViewById(R.id.etName2);
        etAddress = (EditText) findViewById(R.id.etAddress2);
        reviewDate = (EditText) findViewById(R.id.detailDate);
        reviewTitle = (EditText) findViewById(R.id.detailTtile);
        review = (EditText) findViewById(R.id.detailReview);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        Intent intent = getIntent();
        etName.setText(intent.getStringExtra("name"));
        etAddress.setText(intent.getStringExtra("address"));

        ivPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    dispatchTakePictureIntent();
                    return true;
                }
                return false;
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "ddwu.mobile.finalproject.ma01_20200554.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        int targetW = ivPhoto.getWidth();
        int targetH = ivPhoto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ivPhoto.setImageBitmap(bitmap);
    }



    public void onClick(View v){
        switch(v.getId()){
            case R.id.reviewOk:
                SQLiteDatabase db = helper.getWritableDatabase();

                String reviews = review.getText().toString();
                String photo = mCurrentPhotoPath;
                String date = reviewDate.getText().toString();
                String title = reviewTitle.getText().toString();

                ContentValues row = new ContentValues();
                row.put(ReviewDBHelper.DATE, date);
                row.put(ReviewDBHelper.TITLE, title);
                row.put(ReviewDBHelper.REVIEW, reviews);
                row.put(ReviewDBHelper.PHOTO, photo);
                db.insert(ReviewDBHelper.TABLE, null, row);
                Toast.makeText(this, "리뷰를 등록했습니다", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ReviewAddActivity.this, ReviewListActivity.class);

                intent.putExtra("name",etName.toString());
                intent.putExtra("address", etAddress.toString());


                startActivity(intent);
                helper.close();
                finish();
                break;
            case R.id.reviewCancel:
                finish();
                break;

        }
    }

}