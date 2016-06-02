package android.baodian.com.biaodiangithub.tab1;

import android.app.Activity;
import android.baodian.com.biaodiangithub.R;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.util.List;

public class TaskSubmitActivity extends AppCompatActivity {

    private ImageView iv_upoad1;
    private ImageView iv_upoad2;
    private ImageView iv_upoad3;
    private ImagePicker imagePicker;
    private int mPosition = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);
        imagePicker = new ImagePicker(TaskSubmitActivity.this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                                               @Override
                                               public void onImagesChosen(List<ChosenImage> images) {
                                                   // Display images
                                                   File file = new File(images.get(0).getOriginalPath());
                                                   if(mPosition == 1){
                                                       iv_upoad1.setImageURI(Uri.fromFile(file));
                                                   }else if(mPosition == 2){
                                                       iv_upoad2.setImageURI(Uri.fromFile(file));
                                                   }else if(mPosition == 3){
                                                       iv_upoad3.setImageURI(Uri.fromFile(file));
                                                   }
                                               }

                                               @Override
                                               public void onError(String message) {
                                                   // Do error handling
                                               }
                                           }
        );
        iv_upoad1 = (ImageView) findViewById(R.id.iv_upload1);
        iv_upoad2 = (ImageView) findViewById(R.id.iv_upload2);
        iv_upoad3 = (ImageView) findViewById(R.id.iv_upload3);
        iv_upoad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.pickImage();
            }
        });
        iv_upoad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.pickImage();
            }
        });
        iv_upoad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker.pickImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                if(imagePicker == null) {
                    imagePicker = new ImagePicker(TaskSubmitActivity.this);
                    imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                        @Override
                        public void onImagesChosen(List<ChosenImage> list) {

                        }

                        @Override
                        public void onError(String s) {

                        }
                    });
                }
                imagePicker.submit(data);
            }
        }
    }
}
