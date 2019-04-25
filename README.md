# IdeaVim扩展

本插件作为[IdeaVim](https://plugins.jetbrains.com/plugin/164)存在.暂时只有一个功能,就是在退出插入模式时可以切换回系统的英文输入法.

支持Windows，MacOS和Linux
- Windows 需要开启英语美国键盘
- MacOS 需要开启英语美国键盘或ABC键盘
- Linux 需要使用fcitx输入法，通过fcitx-remote切换

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
* 1.3.5
  避免抛出NoSuchFieldException
  
* 1.3.4
  修复某些插入命令无法恢复输入法的问题
  
* 1.3.3
  增加Linux下Fcitx输入法支持，ubuntu 18.04下测试通过，理论上支持所有`fcitx`

* 1.3.2
  修正自动切换输入法功能在旧版IDEA失效问题
  
* 1.3.1
  编辑器重新获得焦点时,如果编辑器处于NORMAL/VISUAL模式,保持输入法为英文状态
  
* 1.3.0
  使用kotlin重写插件
  
* 1.2.3
  利用异步操作避免IDE失去响应
  
* 1.2.2
  修正导致IDE崩溃的bug
  
* 1.2.1
  修正导致不能恢复到英文输入法的问题
  
* 1.2
  增加对Windows的支持, support Windows.
  
* 1.1.5
  增加对macOS的ABC键盘支持. support keylayout ABC.

* 1.1.3
  解决一个偶尔出现的空值异常问题. resolve a NPE problem which happen rarely;

* 1.1.2
  增加英文说明.append information in English.

* 1.1.1
  更改自动注册的按键映射为`:nnoremap <Esc> a<Esc><Esc>`保证在normal模式下按esc切换到英文输入法.并且执行一次默认操作

* 1.1
  自动注册按键映射`:nmap <Esc> a<Esc>`以保证normal模式下可以按esc切换到英文输入法.
  添加回到insert模式恢复为原来的输入方式的能力

* 1.0 
  首次发布,macOS下,退出插入模式可以自动切换到英文输入法

### 感谢
[史荣久](https://github.com/trydofor) 贡献了支持linux的代码

## IdeaVimExtension

The plugin is an extension of 'IdeaVim' , can switch to English input source in normal mode and restore input source in insert mode.

### How To Enable:

In normal mode ,in an editor input `:set keep-english-in-normal` for auto swith to English input source.

use `:set keep-english-in-normal-and-restore-in-insert`, if you want to restore original input source after return insert mode.

Or add the command to the file `~/.ideavimrc`

### Notice:

The plugin only support **macOS** and **Windows**, support **Linux** via fcitx

### Thanks:

[trydofor](https://github.com/trydofor) contributed to support linux code