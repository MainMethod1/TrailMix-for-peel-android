/**
 * 
 */
package com.mainmethod.trailmix1;

import com.mainmethod.trailmix1.sqlite.helper.DatabaseHelper;
import com.mainmethod.trailmix1.sqlite.model.Session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author D4RK
 *
 */
public class HistoryDetailFragment extends Fragment {

	public static final String ARG_SESSION_ID = "id";
	private Session session;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_SESSION_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			// mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
			// ARG_ITEM_ID));
			session = getSession(Integer.parseInt(getArguments().getString(ARG_SESSION_ID)));
		}
	}

	private Session getSession(int id) {
		DatabaseHelper db = new DatabaseHelper(getActivity());
		Session session = db.getSessionById(id);
		db.close();
		return session;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_history_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (session != null) {
			((TextView) rootView.findViewById(R.id.txt_session_speed))
					.setText(String.valueOf(session.getSpeed()));
			((TextView) rootView.findViewById(R.id.txt_session_distance))
			.setText(String.valueOf(session.getDistance()));
			((TextView) rootView.findViewById(R.id.txt_session_duration))
			.setText(MapUtil.formatTime(session.getTime()));
		}

		return rootView;
	}
    
}
