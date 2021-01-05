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
    implementation 'com.github.hanjie511:PickPhotoView:v2.2.2'
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
    <version>2.2.2</version>
</dependency>  
```
## 在项目中引用  
* Step1  在布局文件中添加布局  
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
pickPhotoView.setREQUEST_CODE_CAMERA(int requestCode);\\设置拍照的requestCode
pickPhotoView.setREQUEST_CODE_CHOOSE_PICTURE(int requestCode);\\设置从相册中选择图片的requesCode
pickPhotoView.setREQUEST_CODE_PREVIEW_PICTURE(int requestCode);\\设置预览照片的requestCode
pickPhotoView.setREQUEST_CODE_READ_EXTERNAL_STORAGE(int requestCode);\\设置读取外部存储权限的requestCode
pickPhotoView.setMaxPhotoNumber(int count);//设置最多可以选择图片的数量，可以不用设置，默认为9张
pickPhotoView.setRecyclerViewColumn(int count);//设置显示照片控件的列数
pickPhotoView.setPhotoListChangedListener(new PickPhotoView.PhotoListChangedListener() {
    @Override
    public void getPhotoList(List<String> pathList) {
        //pathList就是PickPhotoView返回的选择照片的文件路径的list
        .  
        .  
        .  
      }
    });
pickPhotoView.initView(Context ctx);  
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
* eg:  
```java  
 pickPhotoView=findViewById(R.id.pickPhotoView);
 pickPhotoView.setMaxPhotoNumber(9);
 pickPhotoView.setREQUEST_CODE_CAMERA(1);
 pickPhotoView.setREQUEST_CODE_CHOOSE_PICTURE(2);
 pickPhotoView.setREQUEST_CODE_PREVIEW_PICTURE(3);
 pickPhotoView.setREQUEST_CODE_READ_EXTERNAL_STORAGE(4);
 pickPhotoView.setPhotoListChangedListener(new PickPhotoView.PhotoListChangedListener() {
  @Override
  public void getPhotoList(List<String> pathList) {
    System.out.println("pathList.size:"+pathList.size());
    }
  });
 pickPhotoView.initView(MainActivity.this);
 pickPhotoView1=findViewById(R.id.pickPhotoView1);
 pickPhotoView1.setMaxPhotoNumber(4);
 pickPhotoView1.setREQUEST_CODE_CAMERA(5);
 pickPhotoView1.setREQUEST_CODE_CHOOSE_PICTURE(6);
 pickPhotoView1.setREQUEST_CODE_PREVIEW_PICTURE(7);
 pickPhotoView1.setREQUEST_CODE_READ_EXTERNAL_STORAGE(8);
 pickPhotoView1.setPhotoListChangedListener(new PickPhotoView.PhotoListChangedListener() {
 @Override
 public void getPhotoList(List<String> pathList) {
  System.out.println("pathList.size1:"+pathList.size());
   }
 });
 }
 pickPhotoView1.initView(MainActivity.this);
 @Override
 protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
 super.onActivityResult(requestCode, resultCode, data);
 pickPhotoView.handleOnActivityResult(requestCode,resultCode,data);
 pickPhotoView1.handleOnActivityResult(requestCode,resultCode,data);
  }
 @Override
 public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 pickPhotoView.handleRequestPermissionResult(requestCode,permissions,grantResults);
 pickPhotoView1.handleRequestPermissionResult(requestCode,permissions,grantResults);
  }
```
* Step4 在引用项目的xml/file_path文件中添加如下内容  
```java  
<external-path
  name="my_images"
  path=""/> 
```  
     
### 通过以上操作，我们就实现了在设备上获取图片的功能，不需要再写其他多余的代码了。  
## 项目中引用的第三方库  
### [uCrop](https://github.com/hanjie511/uCrop) 
### [PhotoView](https://github.com/chrisbanes/PhotoView) 
### [Glide](https://github.com/bumptech/glide) 

