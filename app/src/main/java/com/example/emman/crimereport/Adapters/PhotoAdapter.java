package com.example.emman.crimereport.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.emman.crimereport.Models.Photo;
import com.example.emman.crimereport.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder>{

    ArrayList<Photo> photos;
    Context context;

    public PhotoAdapter() {
        this.photos = new ArrayList<>();
    }

    public PhotoAdapter(Context context) {
        this.context = context;
        this.photos = new ArrayList<>();
    }

    public void updateData(List<Photo> photos){
        this.photos = new ArrayList<>(photos);
        notifyDataSetChanged();
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent,false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {
        Glide.with(holder.itemView).load(photos.get(position).getUrl()).into(holder.mPhotoImage);
        holder.mPhotoDescription.setText(photos.get(position).getDescription());
        holder.mPhotoLocation.setText(photos.get(position).getLocation());

        holder.mImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo photo = photos.get(position);

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());


                Intent shareIntent;

                Bitmap bitmap = ((BitmapDrawable)holder.mPhotoImage.getDrawable()).getBitmap();
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.jpeg";
                OutputStream out = null;
                File file=new File(path);
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path=file.getPath();
                Uri bmpUri = Uri.parse("file://"+path);
                Log.d("SHARE", bmpUri.toString());
                shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT,photo.getDescription() + " ---- " + photo.getLocation());
                shareIntent.setType("image/jpeg");
                context.startActivity(Intent.createChooser(shareIntent,"hola" ));

            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder{

        TextView mPhotoDescription;
        TextView mPhotoLocation;
        ImageView mPhotoImage;
        ImageButton mImagebtn;

        public PhotoHolder(View itemView) {
            super(itemView);

            mPhotoDescription = itemView.findViewById(R.id.photo_description);
            mPhotoLocation = itemView.findViewById(R.id.photo_location);
            mPhotoImage = itemView.findViewById(R.id.photo_image);
            mImagebtn = itemView.findViewById(R.id.share_btn);


        }
    }
}
