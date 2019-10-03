package com.divya.sprxs.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.divya.sprxs.R;
import com.divya.sprxs.model.ChatMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DataAdapterMessages extends RecyclerView.Adapter<DataAdapterMessages.ViewHolder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private List<ChatMessage> chatMessageList;
    private List<String> imageFileName;
    private Context context;
    private Glide GlideApp;
    private String fileName;
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = firebaseDatabase.getReference();

    public DataAdapterMessages(FragmentActivity fragmentActivity, List<ChatMessage> chatMessageList, Context context) {
        this.chatMessageList = chatMessageList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapterMessages.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_messages, parent, false);
            return new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_messages, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterMessages.ViewHolder holder, final int position) {


        String[] extension = new String[]{"jpg", "png", "gif", "jpeg"};

        for (String imageExtension : extension) {

            if (chatMessageList.get(position).getContent().toLowerCase().endsWith(imageExtension)) {

                System.out.println("imageExtension: " + chatMessageList.get(position).getContent());


                if (MSG_TYPE_RIGHT == 0) {
                    final String AllUserFiles = "AllUsersFileData";
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String UID = user.getUid();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final StorageReference pathReference = storageReference.child(AllUserFiles).child(UID).child(chatMessageList.get(position).getContent());
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                GlideApp.with(context)
                                        .load(task.getResult())
                                        .into(holder.message_image);
                                holder.message_image.setScaleType(ImageView.ScaleType.FIT_XY);
                                holder.message_body.setText(View.GONE);


                            }
                        }
                    });
                } else {

                    final String AllUserFiles = "AllUsersFileData";
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    final StorageReference pathReference = storageReference.child(AllUserFiles).child(chatMessageList.get(position).getFromID()).child(chatMessageList.get(position).getContent());
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                GlideApp.with(context)
                                        .load(task.getResult())
                                        .into(holder.message_image);
                                holder.message_image.setScaleType(ImageView.ScaleType.FIT_XY);
                                holder.message_body.setVisibility(View.INVISIBLE);

                            }
                        }
                    });


                }

            }

        }


        String[] extensionFile = new String[]{"doc", "docx", "html", "htm", "odt", "pdf", "xls", "xlsx", "ods", "ppt", "pptx", "txt"};

        for (String fileExtension : extensionFile) {

            if (chatMessageList.get(position).getContent().toLowerCase().endsWith(fileExtension)) {

                holder.message_body.setText(chatMessageList.get(position).getContent());
                SpannableString spannableString = new SpannableString(holder.message_body.getText());
                spannableString.setSpan(new UnderlineSpan(), 0, holder.message_body.getText().length(), 0);
                holder.message_body.setText(spannableString);
                holder.message_body.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String ideaFile = chatMessageList.get(position).getContent();
                        if (MSG_TYPE_RIGHT == 0) {

                            final String AllUserFiles = "AllUsersFileData";
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String UID = user.getUid();
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                            final StorageReference pathReference = storageReference.child(AllUserFiles).child(UID).child(ideaFile);

                            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Toast.makeText(context, "File Downloaded", Toast.LENGTH_LONG).show();

                                    String url = uri.toString();

                                    String[] split = ideaFile.split("\\.");

                                    String fileName = split[0];
                                    String fileExtension = "." + split[1];
                                    String extension = split[1];

                                    downloadFile(context, fileName, fileExtension, DIRECTORY_DOWNLOADS, url);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                        } else {

                            final String AllUserFiles = "AllUsersFileData";
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String UID = user.getUid();
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                            final StorageReference pathReference = storageReference.child(AllUserFiles).child(chatMessageList.get(position).getFromID()).child(ideaFile);

                            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Toast.makeText(context, "File Downloaded", Toast.LENGTH_LONG).show();

                                    String url = uri.toString();

                                    String[] split = ideaFile.split("\\.");

                                    String fileName = split[0];
                                    String fileExtension = "." + split[1];
                                    String extension = split[1];

                                    downloadFile(context, fileName, fileExtension, DIRECTORY_DOWNLOADS, url);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                        }

                    }
                });

                System.out.println("fileExtension: " + chatMessageList.get(position).getContent());
            }
        }
        holder.message_body.setText(chatMessageList.get(position).getContent());

        String[] text = new String[]{"jpg", "png", "gif", "jpeg", "doc", "docx", "html", "htm", "odt", "pdf", "xls", "xlsx", "ods", "ppt", "pptx", "txt"};

        for (String textMessage : text) {

            if (!(chatMessageList.get(position).getContent().toLowerCase()).contentEquals(textMessage)) {

            } else {
                holder.message_body.setText(chatMessageList.get(position).getContent());
                System.out.println("text : " + chatMessageList.get(position).getContent());
            }
        }

    }


    public void downloadFile(Context context, String fileName, String fileExtension, String
            destinationDirectory, String url) {

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
    public int getItemViewType(int position) {

        FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (chatMessageList.get(position).getFromID().equals(firebaseAuth.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {

        return (chatMessageList == null) ? 0 : chatMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message_body;
        ImageView message_image;


        public ViewHolder(View view) {
            super(view);
            message_body = view.findViewById(R.id.message_body);
            message_image = view.findViewById(R.id.message_image);
        }
    }


}
