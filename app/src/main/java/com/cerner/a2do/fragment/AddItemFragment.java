package com.cerner.a2do.fragment;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.cerner.a2do.R;
import com.cerner.a2do.contentProvider.ToDoListContentValueFactory;
import com.cerner.a2do.contentProvider.ToDoListContract;
import com.cerner.a2do.database.ToDoListSQLiteOpenHelper;
import com.cerner.a2do.util.NotifyingAsyncQueryHandler;

import static com.cerner.a2do.fragment.ListActivityFragment.DESCRIPTION_TAG;
import static com.cerner.a2do.fragment.ListActivityFragment.EDIT_TAG;

/**
 * This Fragment contains the logic to Add/Edit the title, description and the due date of the to do list
 *
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddItemFragment extends Fragment implements LoaderManager.LoaderCallbacks, NotifyingAsyncQueryHandler.AsyncQueryListener {
    private static final int INSERT_LIST_TOKEN = 1;
    private static final int TODO_LIST_LOADER = 0;


    private OnFragmentInteractionListener mListener;
    private AsyncQueryHandler asyncQueryHandler;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private EditText titleTextView;
    private EditText taskDescriptionEditText;
    private Button cancelButton;
    private Button saveButton;
    private DatePicker datePicker;

    public String TASK_TITLE_TEXT;
    public String TASK_DESCRIPTION_TEXT;
    public String DUE_DATE;
    public int ID;

    public AddItemFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asyncQueryHandler = new NotifyingAsyncQueryHandler(getContext(), this);
        sqLiteOpenHelper = new ToDoListSQLiteOpenHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        titleTextView = rootView.findViewById(R.id.title_edit_text);
        taskDescriptionEditText = rootView.findViewById(R.id.description_text);
        cancelButton = rootView.findViewById(R.id.cancel_button);
        saveButton = rootView.findViewById(R.id.save_button);
        datePicker = rootView.findViewById(R.id.date_picker);
        onClickListeners();
        if (getTag() != null) {
            if (getTag().equals("EDITED"))
                showEditPage();
        }
        return rootView;
    }

    private void showEditPage() {
        TASK_TITLE_TEXT = getArguments().getString(EDIT_TAG);
        TASK_DESCRIPTION_TEXT = getArguments().getString(DESCRIPTION_TAG);
        titleTextView.setText(TASK_TITLE_TEXT);
        taskDescriptionEditText.setText(TASK_DESCRIPTION_TEXT);
        ID = getArguments().getInt("ID");

    }

    private void onClickListeners() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonOnCLick();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelButtonClicked();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            getLoaderManager().initLoader(TODO_LIST_LOADER, null, this);
        }
    }


    private void saveButtonOnCLick() {
        if (titleTextView != null)
            TASK_TITLE_TEXT = titleTextView.getText().toString();
        if (taskDescriptionEditText != null)
            TASK_DESCRIPTION_TEXT = taskDescriptionEditText.getText().toString();
        if (getTag() != null && getTag().equals("EDITED")) {
            ContentValues contentValues = ToDoListContentValueFactory.updateToDoList(TASK_TITLE_TEXT, TASK_DESCRIPTION_TEXT, null, null);
            asyncQueryHandler.startUpdate(INSERT_LIST_TOKEN, null, ToDoListContract.TODO_LIST.CONTENT_URI, contentValues, ToDoListContract.TODO_LIST._ID + " = ? ", new String[]{String.valueOf(ID)});
        } else {
            ContentValues contentValues = ToDoListContentValueFactory.newToDoListValues(TASK_TITLE_TEXT, TASK_DESCRIPTION_TEXT, null, null);
            asyncQueryHandler.startInsert(INSERT_LIST_TOKEN, null, ToDoListContract.TODO_LIST.CONTENT_URI, contentValues);
        }
    }


    private void onCancelButtonClicked() {
        if (getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStack();
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
        ListActivityFragment listFragment = new ListActivityFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment_activity, listFragment).commit();
    }

    @SuppressLint("Recycle")
    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            titleTextView.setText(TASK_TITLE_TEXT);
            TASK_DESCRIPTION_TEXT = cursor.getString(cursor.getColumnIndex(ToDoListContract.TODO_LIST.TASK_DESCRIPTION));
            taskDescriptionEditText.setText(TASK_DESCRIPTION_TEXT);
        }
    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {
        ListActivityFragment listFragment = new ListActivityFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment_activity, listFragment).commit();
    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
