-- Drop table

-- DROP TABLE public.sys_status_log;

CREATE TABLE IF NOT EXISTS public.sys_status_log (
	id varchar(20) NOT NULL,
	create_time timestamp(6) NULL,
	machine_id varchar(255) NULL,
	available_processors int4 NULL,
	free_swap_space_size int8 NULL,
	total_swap_space_size int8 NULL,
	free_physical_memory_size int8 NULL,
	total_physical_memory_size int8 NULL,
	committed_virtual_memory_size int8 NULL,
	process_cpu_load float8 NULL,
	system_cpu_load float8 NULL,
	process_cpu_time int8 NULL,
	system_load_average float8 NULL,
	CONSTRAINT sys_status_log_pkey PRIMARY KEY (id)
);
