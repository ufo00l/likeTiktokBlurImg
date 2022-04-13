# likeTiktokBlurImg

讲解文章地址：https://juejin.cn/post/7086096022312583182

图片两边或者上下高斯模糊中间清晰效果方案
在平时我们浏览网站时或者在看Tiktok或者抖音上会发现一些图片视频为了适应横竖屏加了两边的高斯模糊。
另外一个场景是应用首页在展示包含横竖页面时，数据是由第三方网站返回的图片样式，服务端不能自己返回统一横竖向图时，我们的列表就需要去适配这种情况。
1.单一图片情况
![Screenshot_20220409_153037.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/fe799407a33443d4862a45309ce3e990~tplv-k3u1fbpfcp-watermark.image?)

![Screenshot_20220409_153049.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bde3ba4cf93c4f9f9b6fc164231b8e43~tplv-k3u1fbpfcp-watermark.image?)
2.模仿首页列表效果
![Screenshot_20220409_152721.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/add7b48fe757433b92b9df74c32130ea~tplv-k3u1fbpfcp-watermark.image?)

需求的要点在于如果是Item是横向的 则接到横图时不做处理，竖图时再进行处理
反之一样。
