# IdeaVim扩展

[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension?ref=badge_shield)

本插件作为[IdeaVim](https://plugins.jetbrains.com/plugin/164)的扩展存在.暂时只有一个功能,就是在退出插入模式时可以切换回系统的英文输入法.

支持Windows，MacOS和Linux

- Windows 需要开启英语美国键盘
- MacOS 需要开启英语美国键盘或ABC键盘
- Linux 需要使用fcitx输入法，通过fcitx-remote切换，或使用ibus输入引擎

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

也可以通过将`set keep-english-in-normal[-and-restore-in-insert]`加入到`~/.ideavimrc`文件中并重启IDE来启用插件功能

在macOS中,normal模式的输入法可以通过变量`keep_input_source_in_normal`来设置,仅支持在`~/.ideavimrc`
中使用.例如`let keep_input_source_in_normal="com.apple.keylayout.ABC"`

在macOS中,normal模式的输入法可以通过变量`keep_input_source_in_normal`来设置,仅支持在`~/.ideavimrc`
中使用.例如`let keep_input_source_in_normal="com.apple.keylayout.ABC"`

ideavim.rc中还可以通过以下三个变量控制插件行为：

`let keep_input_source_in_insert=[input source id]` 设置insert模式使用到非英文输入法

`let keep_input_source_in_normal=[input source id]` 设置normal模式使用到输入英文到输入法

注意：上面两个变量仅在windows和macOS中有效

``` let context_aware=1进入insert模式时根据上下文判断是否恢复输入法，0禁用，1启用```

Linux 下的 fcitx5-rime 可以通过显式申明 `IDEA_VIM_EXTENSION_USE_RIME_ASCII` 环境变量在 normal 模式使用 Rime 的 ASCII Mode。

版本要求：
  - `fcitx5 > 5.0.20`
  - `fcitx5-rime > 5.0.8`

## 更新历史
* 1.6.13

  支持 Linux 下的 fcitx5-rime 使用 Rime ASCII 模式切换输入法。
  需要显式申明 `IDEA_VIM_EXTENSION_USE_RIME_ASCII` 环境变量
   
  - fcitx5 和 fcitx5-rime 版本要求：
    - `fcitx5 > 5.0.20`
    - `fcitx5-rime > 5.0.8`

* 1.6.12

  修正由于配置顺序导致无法恢复输入法的问题

* 1.6.11

  回退到1.6.8的编辑器焦点处理行为 

* 1.6.10
  
  默认激活编辑器焦点事件切换输入法功能 

* 1.6.9

  合并PR<a href="https://github.com/hadix-lin/ideavim_extension/pull/113">#113</a>，当前编辑器失去焦点时恢复输入法

* 1.6.8

  修正问题[#111](https://github.com/hadix-lin/ideavim_extension/issues/111)

* 1.6.7

  兼容IdeaVIM 2.0.0，Idea-IC 2022.3.1+ 

* 1.6.6

  兼容IdeaVIM 2.0.0，Idea-IC 2022.3.1+

* 1.6.6

  兼容IdeaVIM 2.0.0，Idea-IC 2022.3+
  context_aware默认设置为0，对不清楚用法对用户避免产生不稳定的输入发切换行为

* 1.6.5

  修正问题[#96](https://github.com/hadix-lin/ideavim_extension/issues/96)<br/>
  使用独立线程池执行输入法切换动作，避免IDE卡顿

* 1.6.4

  修正问题[#95](https://github.com/hadix-lin/ideavim_extension/issues/95)<br/>
  去除对独立线程池的使用，改为使用应用管理器执行输入法切换动作，减少资源消耗

* 1.6.3 修正非预期情况下切换输入法的问题
* 1.6.2 支持在ideavim.rc中通过以下三个变量控制插件行为：

  `let keep_input_source_in_insert=[input source id]` 设置insert模式使用到非英文输入法

  `let keep_input_source_in_normal=[input source id]` 设置normal模式使用到输入英文到输入法

  注意：上面两个变量仅在windows和macOS中有效

  `let context_aware=1` 进入insert模式时根据上下文判断是否恢复输入法，0禁用，1启用

  注意：仅在`set keep-english-in-normal-and-restore-in-insert`时有意义
* 1.6.1 修正[#87](https://github.com/hadix-lin/ideavim_extension/issues/87)
* 1.6.0 返回insert模式时，根据当前输入位置的字符是否为ASCII来决策是否恢复输入法
* 1.5.2 兼容"IdeaVIM 1.10.0"
* 1.5.0 支持Linux下的IBUS输入引擎，解决[#76](https://github.com/hadix-lin/ideavim_extension/issues/76)
* 1.4.12 修正windows中的npe[问题](https://github.com/hadix-lin/ideavim_extension/issues/72)
* 1.4.11 在macOS中,normal模式的输入法可以通过`keep_input_source_in_normal`来设置
* 1.4.10 兼容"IdeaVIM 0.67"
* 1.4.9 支持apple silicon
* 1.4.8 兼容"IdeaVIM 0.61"
* 1.4.7 在MacOS下支持,Unicode16进制输入法
* 1.4.6 支持fcitx5
* 1.4.5 兼容IdeaVim 0.56, Intellij IDEA 2020.1
* 1.4.4
  修正编辑器重新获取焦点时保存的输入法状态不正确的问题[#48](https://github.com/hadix-lin/ideavim_extension/issues/48)
* 1.4.3 修正非预期的输入法回复问题 [#44](https://github.com/hadix-lin/ideavim_extension/issues/44)
* 1.4.2
    1. 根据vim指令执行后编辑器状态来判断是否需要恢复输入法
    2. 修正某些情况下恢复输入法出错的问题
* 1.4.1 修复某些插入命令无法恢复输入法的问题
* 1.4.0 支持IdeaVim 0.54, 从这个版本起兼容性策略与IdeaVim保持一致
* 1.3.9 支持IdeaVim 0.52
* 1.3.8 支持自定义"Exit Insert Mode"动作的IDE快捷键
* 1.3.7 修正在`~/.ideavimrc`中使用`set keep-english-in-normal-and-restore-in-insert`无效的问题
* 1.3.6 默认启用输入法自动切换功能,无须使用`set xxx`命令
* 1.3.5 避免抛出NoSuchFieldException
* 1.3.4 修复某些插入命令无法恢复输入法的问题
* 1.3.3 增加Linux下Fcitx输入法支持，ubuntu 18.04下测试通过，理论上支持所有`fcitx`
* 1.3.2 修正自动切换输入法功能在旧版IDEA失效问题
* 1.3.1 编辑器重新获得焦点时,如果编辑器处于NORMAL/VISUAL模式,保持输入法为英文状态
* 1.3.0 使用kotlin重写插件
* 1.2.3 利用异步操作避免IDE失去响应
* 1.2.2 修正导致IDE崩溃的bug
* 1.2.1 修正导致不能恢复到英文输入法的问题
* 1.2 增加对Windows的支持, support Windows.
* 1.1.5 增加对macOS的ABC键盘支持. support keylayout ABC.
* 1.1.3 解决一个偶尔出现的空值异常问题. resolve a NPE problem which happen rarely;
* 1.1.2 增加英文说明.append information in English.
* 1.1.1 更改自动注册的按键映射为`:nnoremap <Esc> a<Esc><Esc>`保证在normal模式下按esc切换到英文输入法.并且执行一次默认操作
* 1.1 自动注册按键映射`:nmap <Esc> a<Esc>`以保证normal模式下可以按esc切换到英文输入法. 添加回到insert模式恢复为原来的输入方式的能力
* 1.0 首次发布,macOS下,退出插入模式可以自动切换到英文输入法

### 感谢

[史荣久](https://github.com/trydofor) 贡献了支持linux下的fcitx输入请求的代码
[yangxuanx](https://github.com/yangxuanx) 帮助进行linux环境下的测试
[邓志宇](https://github.com/yuzhou721) 贡献了支持linux下ibus输入引擎的代码

## IdeaVimExtension

The plugin is an extension of 'IdeaVim' , can switch to English input method in normal mode and restore input method in
insert mode.

### How To Enable:

Auto-switch feature is enabled by default

You can also, In normal mode ,in an editor input <code>:set keep-english-in-normal</code> to enable the auto-switch
feature.

Use`:set keep-english-in-normal-and-restore-in-insert` instead, if you want to restore original input method after
return insert mode.

Or add the command to the file `~/.ideavimrc`.

Use `:set nokeep-english-in-normal[-and-restore-in-insert]` to disable the auto-switch feature.

### Notice:

The plugin supports **macOS** and **Windows**, support **Linux** via fcitx

### Thanks:

[trydofor](https://github.com/trydofor) contributed code to support fcitx under linux
[yangxuanx](https://github.com/yangxuanx) helps to test in linux environment
[yuzhou721](https://github.com/yuzhou721) contributed code to support ibus under linux

## License

[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fhadix-lin%2Fideavim_extension?ref=badge_large)