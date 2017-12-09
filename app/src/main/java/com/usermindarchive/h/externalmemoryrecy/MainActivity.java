package com.usermindarchive.h.externalmemoryrecy;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button image,video,song;
    ListView data;
    RecyclerView datarecy;
    List<String> datalist=new ArrayList<String>();
    List<String> dataNames=new ArrayList<String>();
    List<String> filenames=new ArrayList<String>();
    List<String> fileloc=new ArrayList<String>();
    File main;
    TextView msg;
    Toolbar tb;
    EditText txt;

    LinearLayout rel;

    String[] premission= new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image=(Button)findViewById(R.id.img);
        video=(Button)findViewById(R.id.vid);
        song=(Button)findViewById(R.id.song);
        msg=(TextView)findViewById(R.id.textView2);
        rel=(LinearLayout)findViewById(R.id.lay);
        tb=(Toolbar)findViewById(R.id.bar);
        txt=(EditText)findViewById(R.id.srch);
        setSupportActionBar(tb);
//        data=(ListView)findViewById(R.id.data);
        datarecy=(RecyclerView)findViewById(R.id.datarecy);

        if(ActivityCompat.checkSelfPermission(this,premission[0])!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this,premission[1])!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,premission,406);
        }else{
            main= new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()));
            File[]t=main.getParentFile().listFiles();
            for(File dir:t){
                Log.e("Main",dir.getAbsolutePath());
                directory(dir);
            }


        }
//        Directorytype(".mp3");
//        createRecy();

       song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible(msg);
                String tp=".mp3";
                Directorytype(tp);
                createRecy();
               // Adapter(filenames,tp);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible(msg);
                String tp=".mp4";
                Directorytype(tp);
                for(String test:fileloc){
                    Log.e("TEST",test);
                }
                createRecy();

                //Adapter(filenames,tp);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible(msg);
                String tp=".jpg";
                Directorytype(tp);
                createRecy();

                //Adapter(filenames,tp);
            }
        });


        rel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                txt.setVisibility(View.GONE);
                txt.setText(null);
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(txt.getWindowToken(), 0);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater in=getMenuInflater();
        in.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                if(txt.getVisibility()== View.GONE){
                    txt.setVisibility(View.VISIBLE);
                    txt.setHint(null);
                }else if(txt.getVisibility()== View.VISIBLE){
                    txt.setHint("Enter");
                }

                break;


            default:
                break;
        }

        return true;
    }
    public void visible(View view){

        view.setVisibility(View.GONE);

    }

//    public class Image extends AsyncTask<>

    //Creating RecyclerView

    public void createRecy(){

        Data recy=new Data();
        LinearLayoutManager one=new LinearLayoutManager(this);
        one.setOrientation(LinearLayoutManager.VERTICAL);
        datarecy.setLayoutManager(one);
        datarecy.setAdapter(recy);
    }

    public void Adapter(List<String> info,  String type ){
       final Intent next= intenttype(type);
        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,info);
        data.setAdapter(adap);
        data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MainActivity.this,fileloc.get(i),Toast.LENGTH_LONG).show();
                next.putExtra("path",fileloc.get(i));
                if(fileloc.get(i).endsWith(".mp3")){
                    startActivity(next);
                }else{
                    startActivityForResult(next,RESULT_OK);
                }

            }
        });
    }

    public Intent intenttype(String use){
        Intent one = null;
        switch (use){
            case ".jpg":
                 one=new Intent(MainActivity.this,Image.class);
                break;
            case ".mp4":
                one=new Intent(MainActivity.this,VideoTest.class);
                break;
            case ".mp3":
                one=new Intent(MainActivity.this,VideoTest.class);
                break;

        }
        return one;

    }


    public void Directorytype(String fl){
        filenames.clear();
        fileloc.clear();
        for(int i=0;i<dataNames.size();i++){
            if(datalist.get(i).toLowerCase().endsWith(fl)){
                Log.e("test","PATH: "+dataNames.get(i));
                fileloc.add(datalist.get(i));
                filenames.add(dataNames.get(i));

            }

        }


    }
    public void name(String dat){
        dataNames.add(dat);
    }
    public void datary(String dat){
        datalist.add(dat);
    }

    public void directory(File in){
        File[] temp= in.listFiles();

        try {
            for (File dir : temp) {

                filelist(dir);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void filelist(File temp){

            if(temp.isDirectory()){
                Log.e("DIRECTORY","PATH: "+temp.getAbsolutePath());
//                datary("DIRECTORY"+","+"PATH: "+temp.getAbsolutePath());
//                name(temp.getName());
                directory(temp);

            }else{
                Log.e("FILES....","PATH: "+temp.getAbsolutePath());
                datary(temp.getAbsolutePath());
                name(temp.getName());
            }
    }


    //reducing the size of image


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, String resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile( resId, options);
    }
    //reducing the size of image ends here

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==406 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                &&grantResults[1]==PackageManager.PERMISSION_GRANTED){

        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(shouldShowRequestPermissionRationale(premission[0])&&shouldShowRequestPermissionRationale(premission[1])){
                    ActivityCompat.requestPermissions(MainActivity.this,premission,406);
                }else{
                    AlertDialog.Builder dia = new AlertDialog.Builder(MainActivity.this);
                    dia.setMessage("Enable the required Permission for the Application\nGo to Setting and Enable the Permissions");
                    dia.setTitle("PERMISSIONS NEEDED");
                    dia.setPositiveButton("PERMISSION", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    dia.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "PERMISSIONS ARE NOT ENABLED", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = dia.create();
                    dialog.show();
                }
            }
        }
    }

    public class Data extends RecyclerView.Adapter<DataList>{

        @Override
        public DataList onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

            return new DataList(view);
        }

        @Override
        public void onBindViewHolder(DataList holder, int position) {
            Log.e("Position", String.valueOf(position));

            holder.name.setText(filenames.get(position));

            if(fileloc.get(position).toLowerCase().endsWith(".mp3")){
            try {
            MediaMetadataRetriever pic = new MediaMetadataRetriever();
            pic.setDataSource(fileloc.get(position));
            if(pic.getEmbeddedPicture()!=null){
                BitmapDrawable tr=new BitmapDrawable(BitmapFactory.decodeByteArray(pic.getEmbeddedPicture(),0,pic.getEmbeddedPicture().length));
                holder.img.setImageDrawable(tr);
            }else{
                holder.img.setImageResource(android.R.mipmap.sym_def_app_icon);
            }
            pic.release();
            }catch (Exception e){
                e.printStackTrace();
            }}else if(fileloc.get(position).toLowerCase().endsWith(".mp4")){
                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(fileloc.get(position), MediaStore.Video.Thumbnails.MICRO_KIND);
                holder.img.setImageBitmap(bMap);
            }
            else{
//                holder.img.setImageBitmap(BitmapFactory.decodeFile(fileloc.get(position),size ));
                holder.img.setImageBitmap( decodeSampledBitmapFromResource(getResources(), fileloc.get(position), 100, 100));

            }


        }


        @Override
        public int getItemCount() {
            return filenames.size();
        }


    }
    public class DataList extends RecyclerView.ViewHolder {
        TextView name;
        ImageView img;
        public DataList(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            img=(ImageView)itemView.findViewById(R.id.img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Toast.makeText(MainActivity.this,fileloc.get(getAdapterPosition()),Toast.LENGTH_LONG).show();

                    if(fileloc.get(getAdapterPosition()).toLowerCase().endsWith(".mp3")){
                        Intent next= intenttype(".mp3");
                        next.putExtra("path",fileloc.get(getAdapterPosition()));
                        startActivity(next);
                    }else if(fileloc.get(getAdapterPosition()).toLowerCase().endsWith(".mp4")){
                        Intent next= intenttype(".mp4");
                        next.putExtra("path",fileloc.get(getAdapterPosition()));
                        startActivityForResult(next,RESULT_OK);
                    }else if(fileloc.get(getAdapterPosition()).toLowerCase().endsWith("jpg")){
                        Intent next= intenttype(".jpg");
                        next.putExtra("path",fileloc.get(getAdapterPosition()));
                        startActivityForResult(next,RESULT_OK);
                    }else{
                        Toast.makeText(MainActivity.this,"NOT PROGRAMMED",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

}
