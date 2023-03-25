CREATE VIEW "public"."v_file_download_zero" AS SELECT
	file_cursor.ID,
	file_cursor.bucket_id,
	file_cursor.file_id,
	file_cursor.file_name,
	file_cursor.user_id,
	file_cursor.create_time,
	file_cursor.VERSION,
	file_cursor.expiry_time,
	file_cursor.collected_id,
	file_cursor.uname,
	file_cursor.is_delete,
	file_cursor.delete_user_id,
	file_cursor.delete_time,
	file_cursor.download_time,
	file_cursor.download_count 
FROM
	file_cursor 
WHERE
	( NOT ( ( file_cursor.ID ) :: TEXT IN ( SELECT v_file_download_count.cursor_id FROM v_file_download_count ) ) ) 
ORDER BY
	file_cursor.create_time DESC;
