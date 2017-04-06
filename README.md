# IdeaVim扩展

本插件作为[IdeaVim](https://plugins.jetbrains.com/plugin/164)存在.暂时只有一个功能,就是在退出插入模式时可以切换回系统的英文输入法.

由于本人不会写操作系统代码,暂时只找到macOS切换输入法的动态库,所以仅支持macOS.

## 构建/安装方法

本项目使用gradle进行管理.

执行如下命令进行构建

```shell
gradle buildPlugin
```

之后会生成 **build/distributions/IdeaVimExtension-\*.\*.\*.zip**.

通过idea的插件配置对话框选择 **install plugin from disk**即可安装该插件

## 使用

该插件利用 **IdeaVIM**插件的扩展点实现的.安装插件后默认是不启用的,需要启用IdeaVim插件后,在编辑器中输入`:set switch-to-english-when-exit-insert-mode`后生效.或者直接将这句命令加入到**~/.ideavimrc**文件中.
