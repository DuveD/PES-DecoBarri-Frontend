package com.decobarri.decobarri.drawe_menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.Image;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileFragmentInteraction listener;
    EditText name, email;
    TextView id, error;
    Button save, cancel, edit_pass;
    ImageView profileImage, cameraImage;
    String imgDecodableString;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        id = (TextView) view.findViewById(R.id.textView1) ;
        name = (EditText) view.findViewById(R.id.editText3);
        email = (EditText) view.findViewById(R.id.editText5);
        error = (TextView) view.findViewById(R.id.error);
        save = (Button) view.findViewById(R.id.button_save);
        cancel = (Button) view.findViewById(R.id.button_cancel);
        edit_pass = (Button) view.findViewById(R.id.button_pass);
        profileImage = (ImageView) view.findViewById(R.id.imageView3);
        cameraImage = (ImageView) view.findViewById(R.id.imageView4);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        edit_pass.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        cameraImage.setOnClickListener(this);

        EasyImage.configuration(getContext())
                .setCopyTakenPhotosToPublicGalleryAppFolder(false)
                .setCopyPickedImagesToPublicGalleryAppFolder(false)
                .setAllowMultiplePickInGallery(false);

        Bundle args = this.getArguments();
        if(!args.getBoolean("success", true)){
            error.setText("Wrong email");
        }

        if(args != null){
            id.setText(args.getString("id"));
            name.setText(args.getString("name"));
            email.setText(args.getString("email"));
        } else {
            Toast.makeText(getContext(), "Not logged", Toast.LENGTH_LONG);
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getActivity().getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseBody> call = client.downloadImage();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("Response code: " + response.code());
                System.out.println("Response message: " + response.message());
                System.out.println("Response body: " + response.body());

                if (response.isSuccessful()){
                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                    profileImage.setImageBitmap(bm);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Error message: " + t.getMessage());
                System.out.println("Error : " + t.toString());
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragmentInteraction) {
            listener = (ProfileFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_save){
            String in_name = name.getText().toString();
            String in_email = email.getText().toString();

            User u = new User(id.getText().toString(), in_name, in_email);

            listener.EditUser(u);


            error.setVisibility(View.VISIBLE);
        }
        else if (view.getId() == R.id.button_cancel){
            listener.ChangeFragment(3);
        }
        else if (view.getId() == R.id.button_pass){
            listener.ChangeFragment(4);
        }
        else if (view.getId() == R.id.imageView3 || view.getId() == R.id.imageView4){
            EasyImage.openChooserWithGallery(EditProfileFragment.this, "Choose Material Image", 0);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();

            String filePath;
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) {
                filePath = uri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                filePath = cursor.getString(idx);
            }
            File file = new File(filePath);
            //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

            Image image = new Image();
            image.setImage(file);

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(getActivity().getResources().getString(R.string.db_URL))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
            Retrofit retrofit = builder.build();
            UserClient client = retrofit.create(UserClient.class);
            Call<String> call = client.Image(body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    System.out.println("Response code: " + response.code());
                    System.out.println("Response message: " + response.message());
                    System.out.println("Response body: " + response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    System.out.println("Error message: " + t.getMessage());
                    System.out.println("Error : " + t.toString());
                }
            });


            EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    //Some error handling
                }

                @Override
                public void onImagesPicked(List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                    //Handle the images
                    onPhotosReturned(imagesFiles);

                /*profileImage.buildDrawingCache();
                Bitmap bm = profileImage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(getActivity().getResources().getString(R.string.db_URL))
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
                Retrofit retrofit = builder.build();
                UserClient client = retrofit.create(UserClient.class);
                Call<String> call = client.Image(image);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            System.out.println("Response: " + response.body());
                        }
                        System.out.println("Response: " + response.code());
                        System.out.println("Response: " + response.message());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.out.println("Response: " + t.getMessage());
                    }
                });*/

                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    //Cancel handling, you might wanna remove taken photo if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getContext());
                        if (photoFile != null) photoFile.delete();
                    }
                }
            });
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onPhotosReturned(List<File> imagesFiles) {
        if (imagesFiles.size() > 1) System.out.println("There're more than one image!");
        Picasso.with(getContext())
                .load(imagesFiles.get(0))
                .resize(profileImage.getWidth(), profileImage.getHeight())
                .centerCrop()
                .into(profileImage);
    }
}
