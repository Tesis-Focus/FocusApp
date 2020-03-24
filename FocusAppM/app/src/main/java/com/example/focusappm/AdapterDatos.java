package com.example.focusappm;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements ItemTouchHelperAdapter {

    ArrayList<String> listDatos;
    private ItemTouchHelper mTouchHelper;
    private ViewPageAdapter listener;

    public AdapterDatos(ArrayList<String> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String item = listDatos.get(fromPosition);
        listDatos.remove(item);
        listDatos.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onItemSwiped(int position) {
        listDatos.remove(position);
        notifyItemRemoved(position);
    }

    public void setmTouchHelper (ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener {

        TextView dato;
        GestureDetector mGestureDetector;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            dato = itemView.findViewById(R.id.idDato);
            mGestureDetector= new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }

        public void asignarDatos (String datos){
            dato.setText(datos);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
           // mOnNoteListener.onNoteClick(getAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }

    }
}
