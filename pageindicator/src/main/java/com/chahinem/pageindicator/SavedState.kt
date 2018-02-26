package com.chahinem.pageindicator

import android.os.Parcel
import android.os.Parcelable
import android.view.View.BaseSavedState
import org.jetbrains.annotations.NotNull

internal class SavedState : BaseSavedState {
  var count: Int = 0
  var selectedIndex = 0

  constructor(superState: Parcelable) : super(superState)

  private constructor(`in`: Parcel) : super(`in`) {
    this.count = `in`.readInt()
    this.selectedIndex = `in`.readInt()
  }

  override fun writeToParcel(out: Parcel, flags: Int) {
    super.writeToParcel(out, flags)
    out.writeInt(this.count)
    out.writeInt(this.selectedIndex)
  }

  companion object {
    @JvmField
    @NotNull
    val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
      override fun createFromParcel(`in`: Parcel): SavedState {
        return SavedState(`in`)
      }

      override fun newArray(size: Int): Array<SavedState?> {
        return arrayOfNulls(size)
      }
    }
  }
}