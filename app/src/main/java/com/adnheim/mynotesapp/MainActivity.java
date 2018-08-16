package com.adnheim.mynotesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.adnheim.mynotesapp.adapter.NoteAdapter;
import com.adnheim.mynotesapp.db.NoteHelper;
import com.adnheim.mynotesapp.entity.Note;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvNotes;
    ProgressBar progressBar;
    FloatingActionButton fabAdd;

    private LinkedList<Note> list;
    private NoteAdapter adapter;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Notes");

        rvNotes = (RecyclerView)findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        fabAdd = (FloatingActionButton)findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        try {
            noteHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        list = new LinkedList<>();

        adapter = new NoteAdapter(this);
        adapter.setListNotes(list);
        rvNotes.setAdapter(adapter);

        new LoadNoteAsync().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null){
            noteHelper.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if(resultCode == FormAddUpdateActivity.RESULT_ADD){
                new LoadNoteAsync().execute();
                showSnackbarMessage("Satu item berhasil ditambahkan");
            }
        }
        else if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE){
            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                new LoadNoteAsync().execute();
                showSnackbarMessage("Satu item berhasil diubah");
            }
            else if (resultCode == FormAddUpdateActivity.RESULT_DELETE){
                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
                list.remove(position);
                adapter.setListNotes(list);
                adapter.notifyDataSetChanged();
                showSnackbarMessage("Satu item berhasil dihapus");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add){
            Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }

    }

    private void showSnackbarMessage(String message){
        Snackbar.make(rvNotes, message, Snackbar.LENGTH_SHORT).show();
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Note>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            if (list.size() > 0){
                list.clear();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list.addAll(notes);
            adapter.setListNotes(list);
            adapter.notifyDataSetChanged();

            if (list.size() == 0){
                showSnackbarMessage("Tidak ada data saat ini");
            }
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            return noteHelper.query();
        }
    }
}
