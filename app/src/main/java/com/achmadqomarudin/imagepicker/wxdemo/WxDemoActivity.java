package com.achmadqomarudin.imagepicker.wxdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.achmadqomarudin.imagepicker.R;
import com.achmadqomarudin.imagepicker.SelectDialog;
import com.achmadqomarudin.imagepicker.imageloader.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class WxDemoActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //All currently selected pictures
    private int maxImgCount = 8;               //Allow to choose the maximum number of pictures

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxdemo);
        setTitle("Demo");

        initImagePicker();
        initWidget();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());     //Set image loader
        imagePicker.setShowCamera(true);                        //Show photo button
        imagePicker.setCrop(true);                              //Allow cropping (only for single selection)
        imagePicker.setSaveRectangle(true);                     //Whether to save by rectangular area
        imagePicker.setSelectLimit(maxImgCount);                //Selected quantity limit
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);    //Shape of crop box
        imagePicker.setFocusWidth(800);                         //The width of the crop box. Unit pixel (circle automatically takes the minimum width and height)
        imagePicker.setFocusHeight(800);                        //The height of the crop box. Unit pixel (circle automatically takes the minimum width and height)
        imagePicker.setOutPutX(1000);                           //Save the width of the file. Unit pixel
        imagePicker.setOutPutY(1000);                           //Save the height of the file. Unit pixel
    }

    private void initWidget() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("Take a picture");
                names.add("Photo album");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: //Turn up the camera directly
                                /**
                                 * 0.4.7 At present, the camera is directly turned up and does not support cropping. If the crop is turned on, the picture will not be returned. Please note that subsequent versions will solve
                                 *
                                 * However, the currently directly dependent version has been resolved. Considering that the version has changed little, this time it has not been uploaded to the remote warehouse.
                                 *
                                 * If you really need it, please download the source code reference directly.
                                 */
                                //Open the selection, the number of choices allowed this time
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(WxDemoActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // Whether to open the camera directly
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //Open the selection, the number of choices allowed this time
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(WxDemoActivity.this, ImageGridActivity.class);
                                /* If you need to enter the selection to display the selected picture,
                                 * Please check for details ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //Open preview
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //Add picture back
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //Preview picture back
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}
