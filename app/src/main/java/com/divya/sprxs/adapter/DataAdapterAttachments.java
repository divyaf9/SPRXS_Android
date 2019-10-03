package com.divya.sprxs.adapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.model.IdeaFilesResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DataAdapterAttachments extends RecyclerView.Adapter<DataAdapterAttachments.ViewHolder> {

    private List<IdeaFilesResponse> ideaFilesResponseList = new ArrayList<>();
    private CardView cardViewAttachments;
    private Context context;

    public DataAdapterAttachments(FragmentActivity fragmentActivity, List<IdeaFilesResponse> ideaFilesResponseList, Context context){
        this.ideaFilesResponseList = ideaFilesResponseList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapterAttachments.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_attachments, parent, false);
        cardViewAttachments = v.findViewById(R.id.card_view_attachments);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.attachmentTextView.setText(ideaFilesResponseList.get(position).getFilename());

        holder.downloadAttachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ideaFile = ideaFilesResponseList.get(position).getFilename();

                final String AllUserFiles = "AllUsersFileData";
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String UID = user.getUid();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference pathReference = storageReference.child(AllUserFiles).child(UID).child("myIdeas").child(ideaFile);


                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(context,"File Downloaded",Toast.LENGTH_LONG).show();

                        String url = uri.toString();

                        String [] split = ideaFile.split("\\.");

                        String fileName = split[0];
                        String fileExtension = "."+split[1];
                        String extension = split[1];

                        downloadFile(context,fileName,fileExtension, DIRECTORY_DOWNLOADS,url);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        downloadmanager.enqueue(request);

    }

    @Override
    public int getItemCount() {
        return (ideaFilesResponseList == null) ? 0 : ideaFilesResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView attachmentTextView;
        private ImageView downloadAttachments;


        public ViewHolder(final View view) {
            super(view);
            attachmentTextView = view.findViewById(R.id.attachmentTextView);
            downloadAttachments = view.findViewById(R.id.downloadAttachments);


        }
    }
}