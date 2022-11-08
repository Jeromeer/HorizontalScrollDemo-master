#  [V2版本已上传至码云,有需要请移步](https://gitee.com/Jeromeer/horizontal-scroll-demo)
### RecyclerView+HorizontalScrollView 无嵌套实现左侧标题栏固定,右侧内容部分可整体横向滑动,可随意上下拉刷新,刷新和滚动之后,位移位置不变




![gif效果](https://upload-images.jianshu.io/upload_images/3867126-558f2ae7b77c2dd8.gif?imageMogr2/auto-orient/strip)

#### 主要解决以下几点问题
* 1.因整个实现上使用原生RecyclerView,只有右侧内容页横向的RecyclerView+HorizontalScrollView嵌套(单条item横向填充数据量小,嵌套无非不能viewholder复用,所以性能影响忽略不计),无其他自定义view,性能非常好,界面流畅
* 2.解决了右侧部分在viewholder缓存之后,位置错乱问题,即上下滚动出界面之后或者下拉刷新之后,新取出来的holder缓存没有偏移问题
* 3.解决单个item滑动触发整个右测部分滑动的联动和顶部tab单独滑动,联动右侧部分的滑动问题

#### 具体实现方式在博客中,不足之处欢迎交流 [我的简书](https://www.jianshu.com/p/75bba86dd61c)

