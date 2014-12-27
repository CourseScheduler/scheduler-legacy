/**
 * University table describing the university
 * 
 * Primary access methods are primary key and university name
 */
create table university (
	id int not null generated by default as identity,
	name varchar(255) default null,
	constraint university_pkey primary key (id)
);
create index university_ix_name on university(name);

/**
 * Version table describing when the data set was retrieved
 * 
 * Primary access methods are primary key
 */
create table version (
	id int not null generated by default as identity,
	retrieve_date date default null,
	retrieve_time time default null,
	success boolean default false,
	constraint version_pkey primary key (id)
);

/**
 * DateTimeBlock table describing individual date/time blocks
 * 
 * Primary access method is primary key
 */
create table date_time_block (
	id int not null generated by default as identity,
	dow int default null,
	start_time time default null,
	end_time time default null,
	time_zone varchar(255) default null,
	start_date date default null,
	end_date date default null,
	constraint date_time_block_pkey primary key (id)
);

/**
 * Term table describing the registration terms
 * 
 * Primary access methods are primary key and university id, version id
 */
create table term (
	id int not null generated by default as identity,
	university_id int,
	version_id int,
	name varchar(255) default null,
	internal_identifier varchar(255) default null,
	constraint term_pkey primary key (id),
	constraint term_fkey_university_id foreign key (university_id) references university(id) on delete cascade,
	constraint term_fkey_version_id foreign key (version_id) references version(id) on delete cascade
);

/**
 * Section table describing the registration section
 * 
 * Primary access methods are primary key, crn, course, and term id
 */
create table section (
	id int not null generated by default as identity,
	crn varchar(255) default null,
	course varchar(255) default null,
	name varchar(255) default null,
	section varchar(255) default null,
	term_id int,
	constraint section_pkey primary key (id),
	constraint section_fkey_term_id foreign key (term_id) references term(id) on delete cascade
);
create index section_ix_course on section(course);
create index section_ix_crn on section(crn);

/**
 * Meeting table describing the different meetings for sections
 * 
 * Primary access methods are primary key and section id
 */
create table meeting (
	id int not null generated by default as identity,
	date_time_block_id int,
	campus varchar(255) default null,
	building varchar(255) default null,
	room varchar(255) default null,
	meeting_type varchar(255) default null,
	schedule_type varchar(255) default null,
	section_id int,
	constraint meeting_pkey primary key (id),
	constraint meeting_fkey_date_time_block_id foreign key (date_time_block_id) references date_time_block(id) on delete cascade,
	constraint meeting_fkey_section_id foreign key (section_id) references section(id) on delete cascade
);

/**
 * Instructor table describing the known instructors
 * 
 * Primary access methods are id and university id
 */
create table instructor (
	id int not null generated by default as identity,
	name varchar(255) default null,
	constraint instructor_pkey primary key (id)
);
create index instructor_ix_name on instructor(name);

/**
 * Each meeting can have one or more Instructors
 * 
 * Primary access methods are id and meeting id
 */
create table meeting_instructor (
	id int not null generated by default as identity,
	meeting_id int,
	instructor_id int,
	constraint meeting_instructor_pkey primary key (id),
	constraint meeting_instructor_fkey_meeting_id foreign key (meeting_id) references meeting(id) on delete cascade,
	constraint meeting_instructor_fkey_instructor_id foreign key (instructor_id) references instructor(id) on delete cascade
);

/**
 * Each instructor may have multiple rating entries
 * 
 * Primary access methods are id and instructor id
 */
create table instructor_rating (
	id int not null generated by default as identity,
	instructor_id int,
	rating double,
	description varchar(255) default null,
	constraint instructor_rating_pkey primary key (id),
	constraint instructor_rating_fkey_instructor_id foreign key (instructor_id) references instructor(id) on delete cascade
);
