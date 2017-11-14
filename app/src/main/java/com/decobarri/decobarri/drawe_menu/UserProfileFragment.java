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
import android.widget.ViewSwitcher;

import com.decobarri.decobarri.R;
import com.decobarri.decobarri.db_resources.User;

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileFragmentInteraction listener;
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
        edit.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button){
            listener.ChangeFragment(1);
        }
    }
}
