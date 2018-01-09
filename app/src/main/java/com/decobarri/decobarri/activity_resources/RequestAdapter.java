package com.decobarri.decobarri.activity_resources;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Project;
import com.decobarri.decobarri.db_resources.ProjectClient;
import com.decobarri.decobarri.db_resources.Request;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.decobarri.decobarri.db_resources.UserProject;
import com.google.android.gms.common.SignInButton;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Asus on 05/01/2018.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<Request> requestList;
    private RecyclerView recyclerView;
    Context context;
    String username;
    ProjectClient client;
    Retrofit retrofit;

    public RequestAdapter (List<Request> requestList, RecyclerView recyclerView, Context context) {
        this.requestList = requestList;
        this.recyclerView = recyclerView;
        this.context = context;

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        client = retrofit.create(ProjectClient.class);

        SharedPreferences pref = context.getSharedPreferences("LOGGED_USER", MODE_PRIVATE);
        username = pref.getString("username", "");
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView name, project_name;
        ImageView profileImage;
        LinearLayout item_container;
        Button accept, cancel;

        public RequestViewHolder(View view) {
            super(view);
            item_container = (LinearLayout) view.findViewById(R.id.request_container);
            name = (TextView) view.findViewById(R.id.request_name);
            project_name = (TextView) view.findViewById(R.id.project_id);
            profileImage = (ImageView) view.findViewById(R.id.request_imageView);
            accept = (Button) view.findViewById(R.id.accept);
            cancel = (Button) view.findViewById(R.id.cancel);
        }
    }

    public Request deleteRequest (int index) {
        return requestList.remove(index);
    }

    public Request getRequest (int index) {
        return requestList.get(index);
    }

    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);
        RequestViewHolder vh = new RequestViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RequestAdapter.RequestViewHolder holder, final int position) {

        Call<ResponseBody> call = client.getImage(getRequest(position).getUsername());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                    if (bm != null) holder.profileImage.setImageBitmap(
                            Bitmap.createScaledBitmap(bm, holder.profileImage.getWidth(), holder.profileImage.getHeight(), false));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        holder.name.setText(getRequest(position).getUsername());
        holder.project_name.setText("quiere unirse a " + getRequest(position).getName());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = new User();
                user.setId(getRequest(position).getUsername());
                String project = getRequest(position).getProject();
                UserClient userClient = retrofit.create(UserClient.class);
                UserProject project_id = new UserProject(project);
                Call<String> call = userClient.addProject(getRequest(position).getUsername(), project_id);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        System.out.println("Response: " + response.code());
                        System.out.println("Response: " + response.message());
                        if (response.isSuccessful()){
                            deleteRequest(position);
                            notifyDataSetChanged();
                            //cancelRequest(user, getRequest(position).getProject(), position);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setId(getRequest(position).getUsername());
                cancelRequest(user, getRequest(position).getProject(), position);
            }
        });
    }

    private void cancelRequest (User user, String project, final int position) {
        Call<String> call = client.deleteRequest(project, user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    deleteRequest(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
