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

import com.decobarri.decobarri.R;

public class EditPasswordFragment extends Fragment implements View.OnClickListener {

    EditText old_password, new_password;
    Button cancel, save;
    TextView error;
    String username;
    private ProfileFragmentInteraction mListener;

    public EditPasswordFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        old_password=(EditText) view.findViewById(R.id.editText6);
        new_password=(EditText) view.findViewById(R.id.editText7);
        cancel=(Button) view.findViewById(R.id.button5);
        save=(Button) view.findViewById(R.id.button4);
        error=(TextView) view.findViewById(R.id.textView13);

        Bundle args = this.getArguments();
        if(!args.getBoolean("success", true)){
            error.setText("Wrong email");
        }
        username=args.getString("username");


        cancel.setOnClickListener(this);
        save.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileFragmentInteraction) {
            mListener = (ProfileFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button5){ //cancel
            mListener.ChangeFragment(3);
        }
        else if (view.getId() == R.id.button4){ //save
            mListener.EditPassword(username, old_password.getText().toString(), new_password.getText().toString());

            error.setVisibility(View.VISIBLE);
        }
    }
}
