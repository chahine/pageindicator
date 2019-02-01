package com.chahinem.pageindicator.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.chahinem.pageindicator.sample.MyAdapter.MyItem
import com.squareup.picasso.Picasso

class MyPagerAdapter(private val picasso: Picasso,
                     private val items: List<MyItem>) : PagerAdapter() {

  override fun getCount() = items.size

  override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val view = LayoutInflater
        .from(container.context)
        .inflate(R.layout.item_card, container, false)

    val item = items[position]
    val title: TextView = view.findViewById(R.id.title)
    val caption: TextView = view.findViewById(R.id.caption)
    val image: ImageView = view.findViewById(R.id.image)

    picasso
        .load(item.image)
        .placeholder(R.color.colorPrimaryDark)
        .fit()
        .centerCrop()
        .into(image)
    title.text = item.title
    caption.text = item.caption

    container.addView(view)
    return view
  }

  override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
    container.removeView(view as View)
  }
}