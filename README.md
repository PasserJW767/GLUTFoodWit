# 校园点餐Android App

## 目录

- [注册模块与短信验证模块](#1.注册模块与短信验证模块)

- [订单管理模块](#2.订单管理模块)

- [收藏夹管理模块](#3.收藏夹管理模块)

- [足迹管理模块](#4.足迹管理模块)

- [每日抽奖模块](#5.每日抽奖模块)

- [个人信息管理模块](#6.个人信息管理模块)

## 1.注册模块与短信验证模块

### 1.1 UI设计

<img title="" src="assets\pic3.jpg" alt="" data-align="center" width="236">

### 1.2 逻辑实现

注册模块流程图如下：

<img title="" src="assets\pic1.png" alt="" width="473" data-align="center">

**关于正则判断部分：** 在触发输入框`OnFocus`事件时，转入正则表达式判断。判断与要求不符则更改输入框UI效果，无误保持原输入框。

**关于验证码部分：** 使用了Mob平台的SMSSDK，使用流程为：

1. 在Mob的SMSSDK下创建应用，输入应用包名以及程序的MD5签名，这里的MD5签名需要在程序最终完成后，通过手机app获取MD5签名；

2. 在项目中进行配置：
   
   - 在项目build.gradle中：
   
   ```java
   // 添加插件
   apply plugin: 'com.mob.sdk'
   // 注册SMSSDK相关信息
   MobSDK{
      appKey "此处替换为mob官方申请的appkey"
      appSecret "此处替换为mob官方申请的appkey对应的appSecret"
      spEdition "fp" // 设定MobSDK为隐私协议适配版本
      SMSSDK {}
   }
   ```
   
   - 在模块gradle文件中，使用以下内容注册MobSDK：
   
   ```java
   buildscriplt{
       repositories{
           // 1. 添加MobSDK Maven地址
           maven {
               url "https://mvn.mob.com/android"
           }
           ...
       }
       dependencies{
           ...
           // 2.注册MobSDK
           classpath "com.mob.sdk:MobSDK:2018.0319.1724"
       }
   }
   ```

3- 在项目中进行使用，步骤与核心代码如下：

- 定义sendCode方法发送验证码
  
  ```java
  public void sendCode(Context context) {
    RegisterPage page = new RegisterPage();
    //如果使用自己的ui，没有申请模板编号的情况下需传null
    page.setTempCode(null);
    page.setRegisterCallback(new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 处理成功的结果
                HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                // 国家代码，如“86”
                String country = (String) phoneMap.get("country"); 
                // 手机号码，如“13800138000”
                String phone = (String) phoneMap.get("phone"); 
                // 利用国家代码和手机号码进行后续的操作
                ...
            } else{
                // 处理错误的结果
                ...
            }
        }
    });
    page.show(context);
  }
  ```

- 注册监听回调
  
  ```java
  EventHandler eh=new EventHandler(){
  @Override
      public void afterEvent(int event, int result, Object data) {
          // TODO 此处不可直接处理UI线程，处理后续操作需传到主线程中操作
          Message msg = new Message();
          msg.arg1 = event;
          msg.arg2 = result;
          msg.obj = data;
          mHandler.sendMessage(msg);
  
      }
  };
  ```

- 请求验证码
  
  ```java
  SMSSDK.getVerificationCode(country, phone);
  ```

- 提交验证码
  
  ```java
  SMSSDK.submitVerificationCode(country, phone, code);
  ```

- 注销监听
  
  ```java
  // 使用完EventHandler需注销，否则可能出现内存泄漏
  protected void onDestroy() {
    super.onDestroy();
    SMSSDK.unregisterEventHandler(eventHandler);
  }
  ```

## 2.订单管理模块

### 2.1 UI设计

<img title="" src="assets\pic4.jpg" alt="" data-align="inline" width="218">         <img title="" src="assets\pic5.jpg" alt="" width="219">

### 2.2 逻辑实现

这里代码相对比较简单，主要涉及代订单生命周期，生命周期定义如下：

![](assets\pic2.png)

## 3. 收藏夹管理模块

### 3.1 UI设计

<img src="assets\pic6.jpg" title="" alt="" width="241">      <img title="" src="assets\pic7.jpg" alt="" width="242">

### 3.2 逻辑实现

进入页面，会根据数据库中读取的用户的收藏库来判断星星状态。

用户点击星星，根据数据库中的状态对UI的展示进行修改。

![](assets\pic8.png)

## 4. 足迹管理模块

### 4.1 UI设计

<img title="" src="assets\pic10.jpg" alt="" data-align="center" width="277">

### 4.2 逻辑实现

逻辑如下：

- 用户进入店铺时候，将店铺信息插入至足迹库中，此处进行判断。

- 若该用户足迹库中用户足迹小于十条且足迹库中无该店铺，直接插入这个足迹，若小于十条且足迹库中有此店铺，则将当前访问的店铺记录序号更新至最新（保证足迹最新）。

- 若该用户足迹库中用户足迹等于十条时，若无该店铺则删除最旧店铺后插入该新足迹，若等于十条且有此店铺，删除该店铺在足迹的信息后重新插入。

![](assets\pic9.png)

## 5. 每日抽奖模块

### 5.1 UI设计

<img title="" src="assets\pic11.jpg" alt="" data-align="center" width="256">

### 5.2 逻辑实现

核心如下：

```java
private void startAnim() {
        if (!mShouldStartNextTurn) {
            return;
        }
        Random random = new Random();
        int temp;
//        生成双数的随机数，保证不中奖
        while ((temp = random.nextInt(8))%2 == 0){
        }
        setLuckNum(temp);
//        setDuration()设置动画的时长为5s
        ValueAnimator animator = ValueAnimator.ofInt(mStartLuckPosition, mRepeatCount * 8 + mLuckNum)
                .setDuration(5000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int position = (int) animation.getAnimatedValue();
                setCurrentPosition(position % 8);
                mShouldStartNextTurn = false;
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShouldStartNextTurn = true;
                mStartLuckPosition = mLuckNum;
                //最终选中的位置
                if (mLuckAnimationEndListener != null) {
                    mLuckAnimationEndListener.onLuckAnimationEnd(mCurrentPosition,
                            mPrizeDescription[mCurrentPosition]);
                }
            }
        });

        animator.start();
}
```

## 6. 个人信息管理模块

### 6.1 UI设计

<img title="" src="assets\pic12.jpg" alt="" data-align="center" width="275">

### 6.2 逻辑实现

主要是上传头像这一块，首先唤起手机相册，并将数据缓存到本地路径下、日后对本地路径的头像数据进行调取。但是这种方法还不太稳定，比较好的方法是有自己的一个网络存储桶，从存储桶中拉取数据。

其他就是关于密码修改的生命周期图：

<img title="" src="assets\pic13.png" alt="" width="588" data-align="center">
