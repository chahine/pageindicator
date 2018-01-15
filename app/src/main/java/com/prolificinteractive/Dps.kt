package com.prolificinteractive

import android.content.res.Resources

val Int.dp: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Float.dp: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()