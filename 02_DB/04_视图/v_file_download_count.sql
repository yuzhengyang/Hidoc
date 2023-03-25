CREATE VIEW "public"."v_file_download_count" AS SELECT MAX
	( file_download_log.create_time ) AS down_time,
	file_download_log.cursor_id,
	file_download_log.file_name,
	COUNT ( 1 ) AS times 
FROM
	file_download_log 
GROUP BY
	file_download_log.cursor_id,
	file_download_log.file_name 
ORDER BY
	( COUNT ( 1 ) ) DESC;

