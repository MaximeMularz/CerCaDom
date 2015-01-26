package lpa.aumont.cercadom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 *
 */
public class DuoFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = "DUOFRAGMENT_TAG";

    private final String LOG_TAG = DuoFragment.class.getSimpleName();

    private static final String RESPONSES_KEY = "RESPONSES_KEY";
    private static final int QUESTION_VALUE = 1;

    private String mResponse1;
    private String mResponse2;

    private FragmentQuizInteractionListener mListener;
    private Button mButtonLeft;
    private Button mButtonRight;

    public DuoFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set Values
        if (getArguments() != null && getArguments().containsKey(RESPONSES_KEY)) {
            String[] responses = getArguments().getStringArray(RESPONSES_KEY);
            mResponse1 = responses[0];
            mResponse2 = responses[1];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_duo, container, false);

        mButtonLeft = (Button) rootView.findViewById(R.id.duoLeft);
        mButtonRight = (Button) rootView.findViewById(R.id.duoRight);

        //Set listener
        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);

        setValueTextView();

        return rootView;
    }

    private void setValueTextView() {
        mButtonLeft.setText(mResponse1);
        mButtonRight.setText(mResponse2);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentQuizInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateFragmentTexView(Bundle bundle) {
        if (bundle != null && bundle.containsKey(RESPONSES_KEY)) {
            String[] responses = getArguments().getStringArray(RESPONSES_KEY);
            mResponse1 = responses[0];
            mResponse2 = responses[1];
        }
        setValueTextView();
    }

    @Override
    public void onClick(View v) {
        mListener.responseFromFragment(((Button) v).getText().toString(), QUESTION_VALUE);

    }
}
