## Ijkplayer
完整版的ijkplayer的so文件在此。
### 引用ijkplayer的库
##### so文件
在各个module中，命名都是ijkplsyer-cpu型号。或者再ijkplayerso中可以找到所有的so文件。
##### java文件
ijkplayer-java也是要引入的。

### LmnPlayer
```aidl
allprojects {
    		repositories {
	    		...
		    	maven { url 'https://jitpack.io' }
		    }
}
```

```aidl
dependencies {
	        implementation 'com.github.thiagooo0:lmnplayer:v1.0.3'
	}
```
