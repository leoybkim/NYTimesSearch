package com.leoybkim.nytimessearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leoybkim.nytimessearch.R;
import com.leoybkim.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leo on 20/03/17.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item for current position
        Article article = getItem(position);

        // check if existing view is being reused
        // if not using recylce view --> inflate the layout
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_article, parent, false);
        }

        // find the views
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_image);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_headline);

        // clear out recycled views from convertview since last time
        imageView.setImageResource(0);
        textView.setText(article.getHadline());

        // populate the thumbnail image
        // remote download the image in the background using picasso
        String thumbnail = article.getThumbNail();
        // Check if the image exists
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;
    }
}
