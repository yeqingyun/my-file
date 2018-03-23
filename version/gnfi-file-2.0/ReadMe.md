该组件用于上传文件 支持ie10以下浏览器适用springmvc项目目前版本用于单服务上传暂不支持分布式文件上传
但可以继承接口 com.gionee.gnif.file.biz.service.FileService 扩展成分布式**
# 扩展流程 #
继承接口com.gionee.gnif.file.biz.service.FileService实现类
## 如下所示 ##
    
    <bean id="fileService" class="com.gionee.wms.biz.MyFileService"></bean>
    <bean id="fileSysOuterConfig" class="com.gionee.gnif.file.constant.FileSysOuterConfig">
    	<property name="fileService" ref="fileService"></property>
    </bean>
    
通过com.gionee.gnif.file.constant.FileSysOuterConfig可配置扩展接口，以及设置

# 前端接口 #
实现webuploader封装
引入browers中的js和css以及 html可以测试

js提供如下接口

    init 接口用于初始化
    以下接口需在初始化后调用
    start 接口用于启动上传
    stop 	接口用于停止上传
    getFiles 提供获取文件信息
    getUploadedFiles	提供已上传文件数

html中初始化如下

    /**
     * 参数解析
     * swfUrl, //flash 插件地址，用于支持ie10以下浏览器
     * hostUrl, //服务地址 ${base}
     * filePickerDiv, //文件选择按钮 div节点
     * fileListDiv,	//文件列表div节点
     * isMultiple,	//是否是文件多选	默认为多选 选用
     * chunkSize,	//文件分片大小	默认文10M	选用 (如果需要调整需在FileSysOuterConfig进行配置)
     * updateProgressCallback, //文件上传进度条函数，可以配置更好的进度条 进度条可以自己画，由这个去画 选用
     * uploadAllFunc	, //文件 都已经上传完毕后的回调函数   选用
     */
    GnifFileUpload.init(
    		"${base}js/Uploader.swf",
    		"${base}",
    		"filePicker",
    		"fileList",
    		true,
    		10*1024*1024,
    		null,
    		uploadSuccess
    		);

## 附: 自行修改版本引用##
gnif1.0-SNAPSHOT

    <parent>
    	<artifactId>gnif</artifactId>
    	<groupId>com.gionee</groupId>
    	<version>1.0-SNAPSHOT</version>
    </parent>
gnif2.0-SNAPSHOT

    <parent>
    	<artifactId>gnif</artifactId>
    	<groupId>com.gionee</groupId>
    	<version>2.0-SNAPSHOT</version>
    </parent>


