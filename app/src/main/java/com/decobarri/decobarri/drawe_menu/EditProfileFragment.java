package com.decobarri.decobarri.drawe_menu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.User;

public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileFragmentInteraction listener;
    EditText name, email;
    TextView id, error;
    Button save, cancel, edit_pass;

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
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        edit_pass.setOnClickListener(this);



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
    }

}
