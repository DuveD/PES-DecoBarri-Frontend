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
    EditText name, email, old_pass, new_pass;
    TextView id, error;
    Button save, cancel;

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
        old_pass = (EditText) view.findViewById(R.id.editText7);
        new_pass = (EditText) view.findViewById(R.id.editText9);
        error = (TextView) view.findViewById(R.id.error);
        save = (Button) view.findViewById(R.id.button_save);
        cancel = (Button) view.findViewById(R.id.button_cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Bundle args = this.getArguments();

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
            String in_pass = new_pass.getText().toString();
            String in_email = email.getText().toString();
            String pass = old_pass.getText().toString();

            User u = new User(id.getText().toString(), in_name, in_pass, in_email);
            Integer code = listener.ProfileInteraction(2, u, pass);
            if(code!=200){
                error.setText("Invalid old password");
            }
        }
        if (view.getId() == R.id.button_cancel){
            listener.ProfileInteraction(3, null, "");
        }
    }
}
