# takePhoto
简单实现android端从设备上获得图片的功能(适配了android10+的Scope Storage文件存储及访问的新特性)
## 效果预览
* 效果图中第二个图片显示控件是我测试返回的照片路径能否在scope storage新特新下被加载出来而写的一个测试控件。
![项目效果1](./test1_20201010.gif)![项目效果2](./test2_20201010.gif)  
![项目效果3](./test3_20201010.gif)![项目效果4](./test4_20201010.gif)  
## 在项目中添加依赖  
* Gradle  
 1.Add it in your root build.gradle at the end of repositories:
```java  
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
    }  
```  
2.Add the dependency  
```java  
dependencies {
    implementation 'com.github.hanjie511:PickPhotoView:1.0.0'
  }  
```  
* Maven  
```java  
<repositories>
    <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependency>
    <groupId>com.github.hanjie511</groupId>
    <artifactId>PickPhotoView</artifactId>
    <version>1.0.0</version>
</dependency>  
```
## 在项目中引用  
* Step1  在在布局文件中添加布局  
```java  
<com.example.hj.mylibrary.PickPhotoView
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:id="@+id/pickPhotoView"
    />  
```  
* Step2  实例化该控件并设置数据返回的监听接口  
```java  
private PickPhotoView pickPhotoView;
.
.  
pickPhotoView=findViewById(R.id.pickPhotoView);
pickPhotoView.setMaxPhotoNumber(int count);//设置最多可以选择图片的数量，可以不用设置，默认为9张
pickPhotoView.setPhotoListChangedListener(new PickPhotoView.PhotoListChangedListener() {
    @Override
    public void getPhotoList(List<String> pathList) {
        //pathList就是PickPhotoView返回的选择照片的文件路径的list
        .  
        .  
        .  
      }
    });
```  
* Step3 在调用的Activity中重写两个方法  
```java  
@Override
protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    pickPhotoView.handleOnActivityResult(requestCode,resultCode,data);
  }
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    pickPhotoView.handleRequestPermissionResult(requestCode,permissions,grantResults);
  }  
```  
* Step4在引用的项目中添加provider  
```java
<provider
 android:name="androidx.core.content.FileProvider"
 android:authorities="com.example.hj.mylibrary.fileprovider"
 android:exported="false"
 android:grantUriPermissions="true">
 <meta-data
      android:name="android.support.FILE_PROVIDER_PATHS"
      android:resource="@xml/file_path" />
</provider>  
```
* Step5 在xml/file_path文件中添加如下内容  
```java  
<external-path
  name="my_images"
  path="Android/data/${applicationId}/files/Pictures"/> 
```  
     
### 通过以上操作，我们就实现了在设备上获取图片的功能，不需要再写其他多余的代码了。  
## 项目中引用的第三方库  
### [uCrop](https://github.com/hanjie511/uCrop) 
### [PhotoView](https://github.com/chrisbanes/PhotoView) 
### [Glide](https://github.com/bumptech/glide) 

