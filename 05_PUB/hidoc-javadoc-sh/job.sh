
# 开始执行任务
if [ $# -gt 0 ];then

	echo "main: ----------开始----------"
	openApiUrl="http://192.168.14.155:24001/openapi/javadoc/uploadZip/"
	paramName=""
	paramValue=""
	path=""
	name=""
	token=""

	echo "main: ----------获取入参----------"
	for param in "$@"
	do
		paramName=${param%%=*}
		paramValue=${param#*=}
		# echo "main: paramName: $paramName, paramValue: $paramValue"

		if [ "$paramName" = "name" ];then
			name=$paramValue
		elif [ "$paramName" = "path" ];then
			path=$paramValue
		elif [ "$paramName" = "token" ];then
			token=$paramValue
		fi
	done

	echo "main: name: $name"
	echo "main: path: $path"
	echo "main: token: $token"
	
	echo "main: ----------创建工作区----------"
	mkdir workspace
	cd workspace

	echo "main: ----------删除工程目录，重新生成空的目录----------"
        rm -rf ./$name/
	mkdir $name

	echo "main: ----------删除zip包----------"
	rm -rf ./$name.zip
	
	echo "main: ----------从指定目录复制java文件到项目目录----------"
	for file in `find $path -name *.java`
	do
		uuid=$(uuidgen |sed 's/-//g')
		# echo "file: $file, uuid: $uuid"
		cp $file ./$name/$uuid.java
	done

	echo "main: ----------打包zip文件----------"
	zip -q -r $name.zip ./$name


	echo "main: ----------提交到hidoc接口中----------"
	curl -F "file=@$name.zip" -X POST $openApiUrl$token

	echo "main: ----------任务执行完成----------"

else
	echo "main: ----------异常：参数为空，终止任务----------"
fi
