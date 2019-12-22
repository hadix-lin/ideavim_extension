# IdeaVim扩展
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension?ref=badge_shield)


本插件作为[IdeaVim](https://plugins.jetbrains.com/plugin/164)的扩展存在.暂时只有一个功能,就是在退出插入模式时可以切换回系统的英文输入法.

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
输入法自动切换功能**不会**默认启用.

编辑器中normal模式下输入下面的指令来启用自动切换输入法功能：

* `:set keep-english-in-normal` 开启输入法自动切换功能
* `:set keep-english-in-normal-and-restore-in-insert` 回到insert模式时恢复输入法
* `:set nokeep-english-in-normal-and-restore-in-insert` 保留输入法自动切换功能，但是回到insert模式不恢复输入法
* `:set nokeep-english-in-normal` 关闭输入法自动切换功能

也可以通过将`set keep-english-in-normal[-and-restore-in-insert]`加入到`~/.ideavimrc`文件中并重启IDE来启用插件功能。

## 更新历史
* 1.4.3
  修正非预期的输入法回复问题 [#44](https://github.com/hadix-lin/ideavim_extension/issues/44)
  
* 1.4.2
  1. 根据vim指令执行后编辑器状态来判断是否需要恢复输入法
  2. 修正某些情况下恢复输入法出错的问题
  
* 1.4.1
  修复某些插入命令无法恢复输入法的问题
  
* 1.4.0
  支持IdeaVim 0.54, 从这个版本起兼容性策略与IdeaVim保持一致
  
* 1.3.9
  支持IdeaVim 0.52
  
* 1.3.8
  支持自定义"Exit Insert Mode"动作的IDE快捷键
   
* 1.3.7
  修正在`~/.ideavimrc`中使用`set keep-english-in-normal-and-restore-in-insert`无效的问题

* 1.3.6
  默认启用输入法自动切换功能,无须使用`set xxx`命令
  
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

The plugin is an extension of 'IdeaVim' , can switch to English input method in normal mode and restore input method in insert mode.

### How To Enable:

Auto-switch feature is enabled by default

You can also, In normal mode ,in an editor input <code>:set keep-english-in-normal</code> to enable the auto-switch feature.

Use`:set keep-english-in-normal-and-restore-in-insert` instead, if you want to restore original input method after return insert mode.

Or add the command to the file `~/.ideavimrc`.

Use `:set nokeep-english-in-normal[-and-restore-in-insert]` to disable the auto-switch feature.

### Notice:

The plugin supports **macOS** and **Windows**, support **Linux** via fcitx

### Thanks:

[trydofor](https://github.com/trydofor) contributed to support linux code

## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension?ref=badge_large)
