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

通过在编辑器中normal模式下输入 :set keep-english-in-normal 来启用该功能.或者直接将该命令加入到~/.ideavimrc.
如果需要回到insert模式时恢复输入法,请使用 :set keep-english-in-normal-and-restore-in-insert

## 更新历史
* 1.1
  自动注册按键映射`:nmap <Esc> a<Esc>`以保证normal模式下可以按esc切换到英文输入法.
  添加回到insert模式恢复为原来的输入方式的能力
* 1.0 
  首次发布,macOS下,退出插入模式可以自动切换到英文输入法
