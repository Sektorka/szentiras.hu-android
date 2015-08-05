package hu.sektor.bible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class FolderSelectorAdapter<T extends File> extends ArrayAdapter<T>{
    protected LayoutInflater inflater;
    protected int resource, textViewResourceId;
    protected ArrayList<T> items;

    public FolderSelectorAdapter(Context context, int resource, int textViewResourceId, ArrayList<T> items) {
        super(context, resource, textViewResourceId, items);
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        this.resource = resource;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    protected View createViewFromResource(int position, View convertView, ViewGroup parent){
        View view = (convertView == null ? inflater.inflate(resource, parent, false) : convertView);

        TextView textViewResource = (TextView)view.findViewById(textViewResourceId);
        ImageView imageIcon = (ImageView) view.findViewById(R.id.imageIcon);

        T item = getItem(position);

        textViewResource.setText(item.getName());
        imageIcon.setImageResource(item.isDirectory() ? R.drawable.folder : R.drawable.json);

        return view;
    }
}
