package com.example.joutiaapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joutiaapp.CommandeActivity;
import com.example.joutiaapp.Models.Commande;
import com.example.joutiaapp.Models.PanierUser;
import com.example.joutiaapp.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final ArrayList<Commande> groupList;

    public ExpandableListAdapter(Context context, ArrayList<Commande> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // Chaque groupe a une RecyclerView comme enfant
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);
        }

        TextView textViewGroupTitle = convertView.findViewById(R.id.textViewGroupTitle);
        textViewGroupTitle.setText("Commande : "+groupList.get(groupPosition).prixcomplet+" "+"Dhs");

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_item, parent, false);
        }
        RecyclerView recyclerViewChild = convertView.findViewById(R.id.recyclerViewChild);
        recyclerViewChild.setLayoutManager(new LinearLayoutManager(context));

        // Passez uniquement la liste des enfants du groupe actuel
        ArrayList<PanierUser> childList = groupList.get(groupPosition).panierUsers;
        recyclerViewChild.setAdapter(new ChildAdapter(childList, context));

        recyclerViewChild.setNestedScrollingEnabled(false); // Désactive le défilement interne
        setRecyclerViewHeightBasedOnChildren(recyclerViewChild); // Ajuste dynamiquement la hauteur

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            View listItem = recyclerView.getLayoutManager().findViewByPosition(i);
            if (listItem != null) {
                listItem.measure(
                        View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                );
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight + (recyclerView.getItemDecorationCount() * 10); // Ajustez la marge si nécessaire
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
    }
}

