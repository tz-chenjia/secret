# secret
	个人私密内容存储工具
	一个加密的备忘录 
	(适用于有一点数据库基础的人使用) 

## 起源 
	个人账号密码很多几十个， 
	有人说现在都是用手机短信登录还用什么账号密码，
	手机是个不定数，如果换了就比较麻烦了， 
	个人觉得邮箱比较好点。 
	也有人说现在都是关联登录用QQ用微信， 
	但不是所有平台都直接关联登录的， 
	比如Google,微软邮箱等等， 
	账号密码多了， 
	有的平台很长时间都不用， 
	时间长了难免会忘记。
	于是想找个地方，
	把我所有的账号都记录下来，
	起初我是记录在印象笔记中的， 
	但是我不敢把密码写成明文，
	因为印象数据都是存放在公网上的， 
	万一被人看到，那不是全被盗了。
	于是我就想，要是能有个单机的，
	让我能存放秘密信息的地方就好了。
	于是他就诞生了，我给他取名叫“Secret”，中文意思“秘密”
	
	当然他不只是用于存储账号密码，只要是私密的文本格式的文件都可以存储

## Secret的优点 
	Secret 的存储信息是存放在用户自己的数据库中的（需要有一点数据库基础） 
	Secret 的所有信息是加密存储的 
	Secret 属于单机工具相对安全 
	Secret 可以本地备份及邮箱备份，数据不会丢失 
	Secret 操作简单，功能简单 

## Secret的缺点 
	Secret 本身的账号密码以及加密数据，需要用户记住，否则无法找回 
	
## 运行 
	1.启动程序 
	2.配置你自己的数据库 
	3.设置你的账号及密码 (账号设置你已存在的有效的邮箱地址，后期可以发邮件备份你的数据)
	4.登录后就可以使用了 
	
## 基本操作可以使用命令行也可以使用快捷按钮<br> 
	快捷按钮   		命令                                        

	增        	新增存储信息 PUT              						 				

	删        	删除存储的信息 REMOVE         			 
	
	改        	更新存储信息 EDIT             						 	

	查         	查看存储的信息 FIND           						 	

	清		清空会话框 CLEAR              						 

	密		修改密码及加密数字 PASSWORD                  

	备		备份数据 BACKUPS                                     

			获取本地备份路径 FILEPATH				 

	格		格式化全部数据 FORMAT          				 

	退		退出登录 LOGINOUT             					 

			退出系统 OUT

	帮		查看帮助信息 HELP              					 

	

