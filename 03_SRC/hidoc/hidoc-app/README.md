# 简介



# 延伸

从组织角度考虑管理方式

知识库创建
1. 创建知识库
2. 设置知识库属性，团队成员
3. 创建知识库同时，创建相应的文件管理库
4. 资源和配额都使用创建者账户？？？

知识库维护
1. 用户根据权限维护知识库内容
2. 


```



数据修复：
1、图片地址修复
UPDATE doc SET content = REPLACE(content, 'http://10.16.13.183:24001/f/d/u/', 'http://10.16.12.174:24001/f/d/u/');
UPDATE doc_history SET content = REPLACE(content, 'http://10.16.13.183:24001/f/d/u/', 'http://10.16.12.174:24001/f/d/u/');

2、文件路径修复（windows和linux路径不同）
UPDATE sys_file SET content = REPLACE(path, '\', '/');
```


```sql
查询温度信息
SELECT t.create_time,
MAX(temperature) max_tp,MIN(temperature) min_tp,ROUND(AVG(temperature),2) avg_tp,
MAX(humidity) max_hd,MIN(humidity) min_hd,ROUND(AVG(humidity),2) avg_hd
FROM (
	SELECT 
	to_char(create_time , 'YYYY-MM-DD HH24') create_time,
	(data->>'temperature')::int4 AS temperature ,
	(data->>'humidity')::int4 AS humidity 
	FROM data_coll 
	WHERE sender_id = 'qd-220223' AND (data->>'version') = 'v4' AND (data->>'temperature')::int4 > -20
	ORDER BY create_time 
) t GROUP BY create_time 

SELECT t.create_time,
MAX(temperature) max_tp,MIN(temperature) min_tp,ROUND(AVG(temperature),2) avg_tp,
MAX(humidity) max_hd,MIN(humidity) min_hd,ROUND(AVG(humidity),2) avg_hd
FROM (
	SELECT 
	to_char(create_time , 'HH24:SS') create_time ,
	(data->>'temperature')::int4 AS temperature ,
	(data->>'humidity')::int4 AS humidity 
	FROM data_coll 
	WHERE 
		sender_id = 'qd-220223' AND 
		(data->>'version') = 'v4' AND 
		(data->>'temperature')::int4 > -20 AND 
		create_time::date = CURRENT_DATE
	ORDER BY create_time 
) t GROUP BY create_time 
```


sh
```shell script

nohup java -Dfile.encoding=utf-8 -jar -server -Xmx128m -XX:+UseG1GC hidoc-app-0.0.1-SNAPSHOT.jar --spring.config.location=application.yml> ./nohup_output &

```
