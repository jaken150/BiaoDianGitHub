package android.baodian.com.biaodiangithub.tab1;

import android.app.Activity;
import android.baodian.com.biaodiangithub.MainApp;
import android.baodian.com.biaodiangithub.R;
import android.baodian.com.biaodiangithub.entity.BaseResp;
import android.baodian.com.biaodiangithub.entity.GetTaskInfoResp;
import android.baodian.com.biaodiangithub.util.AppConstant;
import android.baodian.com.biaodiangithub.util.DL;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaskSubmitActivity extends AppCompatActivity {

    private String TAG = "TaskSubmitActivity";
    private ImageView iv_upoad1, iv_upoad2, iv_upoad3;
    //    private Button btn_submit;
    private ImagePicker imagePicker;
    private int mPosition = 1;
    private String mPath1 = null, mPath2 = null, mPath3 = null;
    private SweetAlertDialog pDialog;
    private Bitmap bitmap1, bitmap2, bitmap3;
    private Handler mHandler = new Handler(Looper.myLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_submit);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("请稍候");
        pDialog.setCancelable(true);
        imagePicker = new ImagePicker(TaskSubmitActivity.this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                                               @Override
                                               public void onImagesChosen(List<ChosenImage> images) {
                                                   // Display images
                                                   File file = new File(images.get(0).getOriginalPath());
                                                   Uri fileUri = Uri.fromFile(file);
                                                   ImageSize imageSize = new ImageSize(600, 800);
                                                   DisplayImageOptions options = new DisplayImageOptions.Builder()
                                                           .bitmapConfig(Bitmap.Config.ARGB_4444) // default
                                                           .build();
                                                   if (mPosition == 1) {
                                                       mPath1 = images.get(0).getOriginalPath();
                                                       bitmap1 = ImageLoader.getInstance().loadImageSync(fileUri.toString(), imageSize, options);
                                                       ImageLoader.getInstance().displayImage(fileUri.toString(), iv_upoad1);
                                                   } else if (mPosition == 2) {
                                                       mPath2 = images.get(0).getOriginalPath();
                                                       bitmap2 = ImageLoader.getInstance().loadImageSync(fileUri.toString(), imageSize, options);
                                                       ImageLoader.getInstance().displayImage(fileUri.toString(), iv_upoad2);
                                                   } else if (mPosition == 3) {
                                                       mPath3 = images.get(0).getOriginalPath();
                                                       bitmap3 = ImageLoader.getInstance().loadImageSync(fileUri.toString(), imageSize, options);
                                                       ImageLoader.getInstance().displayImage(fileUri.toString(), iv_upoad3);
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
                mPosition = 1;
                imagePicker.pickImage();
            }
        });
        iv_upoad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = 2;
                imagePicker.pickImage();
            }
        });
        iv_upoad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = 3;
                imagePicker.pickImage();
            }
        });

//        btn_submit = (BootstrapButton)findViewById(R.id.btn_sumbit);
        findViewById(R.id.btn_sumbit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DL.log(TAG, "onClick");
                try {
                    pDialog.show();
                    JSONObject jsonObject = new JSONObject();
                    // TODO: 6/4/16
                    jsonObject.put("phone", "13763319124");
                    jsonObject.put("taskid", "123456789");
                    DL.log(TAG, "mPath1 = " + mPath1);
                    if (mPath1 != null && mPath1.length() > 0)
                        jsonObject.put("image1", bitmaptoString(bitmap1));
                    else jsonObject.put("image1", "");
                    if (mPath2 != null && mPath2.length() > 0)
                        jsonObject.put("image2", bitmaptoString(bitmap2));
                    else jsonObject.put("image2", "");
                    if (mPath3 != null && mPath3.length() > 0)
                        jsonObject.put("image3", bitmaptoString(bitmap3));
                    else jsonObject.put("image3", "");
                    if (mPath1 == null && mPath2 == null && mPath3 == null) {
                        DL.toast(TaskSubmitActivity.this, "请先选择图片");
                        pDialog.dismiss();
                        return;
                    }
                    MainApp.getInstance().okHttpPost(AppConstant.URL_SUMBIT_TASK, jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            pDialog.setTitleText("系统繁忙，请稍候再试");
                            DL.log(TAG, "onFailure");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            pDialog.dismiss();
                            final String resp = response.body().string();
                            Logger.json(resp);

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    BaseResp respObj = JSON.parseObject(resp, BaseResp.class);
                                    if (!respObj.checkErrorCode()) {
                                        pDialog = new SweetAlertDialog(TaskSubmitActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        pDialog.setTitleText(respObj.getErrorMsg());
                                        pDialog.show();
                                        return;
                                    } else {
                                        pDialog = new SweetAlertDialog(TaskSubmitActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                        pDialog.setTitleText("提交成功");
                                        pDialog.show();
                                        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        });

                                    }
                                }
                            });


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    pDialog = new SweetAlertDialog(TaskSubmitActivity.this, SweetAlertDialog.ERROR_TYPE);
                    pDialog.setTitleText("图片上传失败，请检查图片是否过大！");
                    pDialog.show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(TaskSubmitActivity.this);
                    imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                        @Override
                        public void onImagesChosen(List<ChosenImage> list) {
                            File file = new File(list.get(0).getOriginalPath());
                            if (mPosition == 1) {
                                iv_upoad1.setImageURI(Uri.fromFile(file));
                            } else if (mPosition == 2) {
                                iv_upoad2.setImageURI(Uri.fromFile(file));
                            } else if (mPosition == 3) {
                                iv_upoad3.setImageURI(Uri.fromFile(file));
                            }
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

    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public String bitmaptoString(Bitmap bitmap) {
        // 将Bitmap转换成字符串

        String string = null;

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);

        byte[] bytes = bStream.toByteArray();

        string = Base64.encodeToString(bytes, Base64.DEFAULT);

        return string;

    }
}
