package com.decobarri.decobarri.drawe_menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.User;
import com.decobarri.decobarri.db_resources.UserClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileFragmentInteraction listener;
    ImageView profile_image;
    TextView id, name, email;
    Button edit;

    public UserProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        id = (TextView) view.findViewById(R.id.textView1);
        name = (TextView) view.findViewById(R.id.textView3);
        email = (TextView) view.findViewById(R.id.textView5);
        edit = (Button) view.findViewById(R.id.button);
        profile_image= (ImageView) view.findViewById(R.id.imageView3);
        edit.setOnClickListener(this);

        Bundle args = this.getArguments();

        if(args != null){
            id.setText(args.getString("id"));
            name.setText(args.getString("name"));
            email.setText(args.getString("email"));
            User user = ((AccountSettingsActivity) this.getActivity()).user;
            String image = user.getImage();
            if(image!=null) {
                Bitmap bm = stringToBitMap(image);
                profile_image.setImageBitmap(bm);
            }
            else profile_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_account_image));
        } else {
            Toast.makeText(getContext(), "Not logged", Toast.LENGTH_LONG);
        }

        /*Picasso.with(getContext())
                .load(this.getResources().getString(R.string.db_URL) + "/user/getImage/{username}")
                .resize(200, 200)
                .centerCrop()
                .into(profile_image);*/
        cargar_imagen();

        return view;
    }

    public Bitmap stringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private void cargar_imagen() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(this.getResources().getString(R.string.db_URL))
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<ResponseBody> call = client.downloadImage(id.getText().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                    /*FileOutputStream f=null;
                    bm.compress(Bitmap.CompressFormat.PNG, 100, f);*/
                    if(bm!=null)profile_image.setImageBitmap(
                            Bitmap.createScaledBitmap(bm, profile_image.getWidth(), profile_image.getHeight(), false));

                    /*Picasso.with(getContext())
                            .load(f)
                            .resize(profile_image.getWidth(), profile_image.getHeight())
                            .centerCrop()
                            .into(profile_image);*/
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button){
            listener.ChangeFragment(1);
        }
    }
}
