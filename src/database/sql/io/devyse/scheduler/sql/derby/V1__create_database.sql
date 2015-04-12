/**
 * V1__create_database.sql
 * 
 * Create the database schema for the new relational data model
 */


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
 * Primary access methods are primary key then the retrieve date and time
 */
create table version (
	id int not null generated by default as identity,
	retrieval_time timestamp default null,
	complete boolean default false,
	constraint version_pkey primary key (id)
);
create index version_ix_retrieve_date_retrieve_time on version(retrieval_time);

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
 * Primary access methods are primary key and university id
 */
create table term (
	id int not null generated by default as identity,
	university_id int,
	name varchar(255) default null,
	internal_identifier varchar(255) default null,
	constraint term_pkey primary key (id),
	constraint term_fkey_university_id foreign key (university_id) references university(id) on delete cascade
);

/**
 * Term dataset table connecting term data to a term and to a specific instance in time (version)
 * 
 * Primary access methods via primary key and term id, possibly version id though more likely ordered by version
 */
create table term_dataset (
	id int not null generated by default as identity,
	term_id int,
	version_id int,
	constraint term_dataset_pkey primary key (id),
	constraint term_dataset_fkey_term_id foreign key (term_id) references term(id) on delete cascade,
	constraint term_dataset_fkey_version_id foreign key (version_id) references version(id) on delete cascade
);

/**
 * Section table describing the registration section
 * 
 * Primary access methods are primary key, crn, course, and term dataset
 */
create table section (
	id int not null generated by default as identity,
	crn varchar(255) default null,
	course_number varchar(255) default null,
	description varchar(255) default null, /*TODO should this be longer than 255 chars?*/
	name varchar(255) default null,
	section_number varchar(255) default null,
	term_data_set_id int,
	constraint section_pkey primary key (id),
	constraint section_fkey_term_data_set_id foreign key (term_data_set_id) references term_dataset(id) on delete cascade
);
create index section_ix_course_number on section(course_number);
create index section_ix_course_number_section_number on section(course_number, section_number);
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
	term_data_set_id int,
	constraint instructor_pkey primary key (id),
	constraint instructor_fkey_term_data_set_id foreign key (term_data_set_id) references term_dataset(id) on delete cascade
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


/**
 * TODO schedule persistence data model
 * 
 * A schedule request contains the parameters for computing available combinations of courses
 * 		the type of schedule request - student
 * 		the list of required courses or sections
 * 		the list of optional complimentary course or section sets
 * 		the minimum / maximum number of courses or credit hours
 * 		
 * A schedule contains the set of sections that satisfies a schedule request
 * 
 */