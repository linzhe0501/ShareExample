package com.example.lin.shareexample;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class PictureTool {
    /**
     * 返回图片路径列表
     */
    public static List<String> queryGallery(Activity activity) {

        List<String> galleryList = new ArrayList<String>();  // 图片路径列表

        List<String> imageFolderIds = new ArrayList<String>(); // 包含图片的文件夹ID列表

        ContentResolver cr = activity.getContentResolver();

        String[] columns = {MediaStore.Images.Media.DATA,   // 图片绝对路径

                MediaStore.Images.Media.BUCKET_ID,    // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名

                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,  // 直接包含该图片文件的文件夹名

                "COUNT(1) AS count"     // 统计当前文件夹下共有多少张图片

        };

        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED; //默认升序排列

        Cursor cur = cr.query(

                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  //MediaStore多媒体数据库中SD卡上的image数据表的uri

                columns,

                null,

                null,

                sortOrder);

        while (cur.moveToNext()) {

            int image_id_column = cur.getColumnIndex(MediaStore.Images.Media.DATA);

            int bucket_id_column = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);

            int bucket_name_column = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int count_column = cur.getColumnIndex("count");



            String image_path = cur.getString(image_id_column); //文件路径

            int bucket_id = cur.getInt(bucket_id_column);  //所在文件夹ID

            String bucket_name = cur.getString(bucket_name_column); //所在文件夹Name

            int count = cur.getInt(count_column);    //当前文件夹下共有多少张图片



            if(count > 1) {

                imageFolderIds.add(String.valueOf(bucket_id));

            } else {

                galleryList.add(image_path);

            }

        }



        int folderCounts = imageFolderIds.size();

        if( folderCounts > 0 ) {

            for ( int i = 0; i < folderCounts; ++i ) {

                String[] projection = {MediaStore.Images.Thumbnails.DATA};

                cur = cr.query(

                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,

                        projection,

                        MediaStore.Images.Media.BUCKET_ID + " = ?",  //查询条件

                        new String[] {imageFolderIds.get(i)},  //查询条件中问号对应的值

                        sortOrder);

                while (cur.moveToNext()) {

                    int image_id_column = cur.getColumnIndex(MediaStore.Images.Media.DATA);

                    String image_path = cur.getString(image_id_column); //文件路径

                    galleryList.add(image_path);

                }

            }

        }

        if( null != cur && !cur.isClosed() ){

            cur.close();

        }

        return galleryList;

    }
}
