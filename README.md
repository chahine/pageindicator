# Page Indicator

An Instagram like page indicator compatible with [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html) and [ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html).

![Preview](/art/pageindicator.gif)

# Setup

__Step 1.__ Add the JitPack repository to your build file
```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```
__Step 2.__ Add the dependency

```groovy
dependencies {
  compile 'com.github.chahinem.pageindicator:pageindicator:0.2.3'
}
```

# Usage

Add the `PageIndicator` to your XML file:

```xml
  <com.chahinem.pageindicator.PageIndicator
      android:id="@+id/pageIndicator"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      />
```

#### RecyclerView

```kotlin
  pageIndicator.attachTo(recyclerView)
```

By default, attaching to a RecyclerView will end up updating the pageIndicator when the most visible item position changes and expect the RecyclerView items to have the same width. 
If you would like to customize this behavior, add a scroll listener to your RecyclerView and use `PageIndicator::swipeNext` and `PageIndicator::swipePrevious`; and set the pageIndicator's count

#### View Pager

```kotlin
  pageIndicator.attachTo(viewPager)
```

#### Manual
```kotlin
  pageIndicator.swipePrevious()
  pageIndicator.swipeNext()
```

# Customization

| Attribute           | Note                                      | Default     |
|---------------------|-------------------------------------------|-------------|
| piDotSpacing        | Spacing between dots                      | 3dp         |
| piDotBound          | Range in which the selected dot remains   | 40dp        |
| piSize[1-6]         | Size from smallest to largest dot         | .5dp - 6dp  |
| piAnimDuration      | Duration of animation* in ms              | 200         |
| piAnimInterpolator  | Animation interpolator*                   | decelerate  |
| piDefaultColor      | Default unselected dot color              | #B2B2B2     |
| piSelectedColor     | Selected dot color                        | #3897F0     |


*Note: the animation duration and interpolator are shared between the scroll animation and dot scaling animation.

## License

```
MIT License

Copyright (c) 2018 Chahine Mouhamad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```
