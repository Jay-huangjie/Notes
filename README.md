##记账本
记账本是一款仿制支付宝记账本模块开发的项目，采用 greenrobot团队的全新数据库框架[objectbox-java](https://github.com/objectbox/objectbox-java)开发完成。

## Project screenshot
##### 首页:
![](http://p2p0lrpx1.bkt.clouddn.com/main.gif-gif)
##### 图表:
![类别报表](http://p2p0lrpx1.bkt.clouddn.com/class.gif-gif "类别报表")
##### 记一笔页面:
![记一笔](http://p2p0lrpx1.bkt.clouddn.com/take.gif-gif "记一笔")

## Points
记账本是纯单机工具类应用，不存在网络请求

* 产品逻辑完全仿照支付宝实现
* 使用RxJava进行数据查询操作
* RxBus 代替 EventBus 进行组件之间通讯
* 使用objectbox数据库实现数据库存储逻辑
* 类别报表饼图使用[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)框架
* 遵循MVP架构
* 完整的基类搭建（BaseActivity,BaseApp,BaseFragment）
* 自定义数字键盘
* RecycleView+ViewPage实现的图标选择界面，支持自定义添加,对外提供接口获取选取的图标数据
* 首页粘性头部使用的oubowu的[StickyItemDecoration](https://github.com/oubowu/StickyItemDecoration)实现，感谢
* SVG图片的应用与适配

## Follow up target
* [ ] 优化饼图算法，使动画更流畅
* [ ] UI的一些美化
* [ ] 收入功能的开发
* [ ] 图片记录的功能开发(拍照，选择图片,图片保存)
* [ ] 记一笔页面的日期选择功能
* [ ] 进行一次完善的单元测试与压力测试

## Update log
```
2018/1/17
提交项目
```

## Statement
注意：此开源项目仅做学习交流使用, 不可用于任何商业用途. 如果你觉得不错, 对你有帮助, 欢迎点个 fork, star, follow , 也可以帮忙分享给你更多的朋友, 这是给作者最大的动力与支持



